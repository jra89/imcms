
import imcode.server.IMCServiceInterface;
import imcode.server.User;
import imcode.util.Check;
import imcode.util.IMCServiceRMI;
import imcode.util.Parser;
import imcode.util.Utility;
import org.apache.log4j.Category;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Vector;

public class TemplateChange extends HttpServlet {

    private static Category log = Category.getInstance(TemplateChange.class.getName());

    public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        String host = req.getHeader("host");
        IMCServiceInterface imcref = IMCServiceRMI.getIMCServiceInterfaceByHost(host);
        String start_url = imcref.getStartUrl();

        // Check if user logged on
        User user;

        if ((user = Check.userLoggedOn(req, res, start_url)) == null) {
            return;
        }

        if (!imcref.checkAdminRights(user)) {
            Utility.redirect(req, res, start_url);
            return;
        }

        res.setContentType("text/html");

        ServletOutputStream out = res.getOutputStream();
        String htmlStr = null;
        String lang_prefix = user.getLangPrefix();
        String lang = req.getParameter("language");
        if (req.getParameter("cancel") != null) {
            Utility.redirect(req, res, "TemplateAdmin");
            return;
        } else if (req.getParameter("template_get") != null) {
            int template_id = Integer.parseInt(req.getParameter("template"));
            String filename = imcref.sqlQueryStr("select template_name from templates where template_id = ?", new String[]{"" + template_id});
            if (filename == null) {
                filename = "";
            }
            byte[] file = imcref.getTemplateData(template_id);
            res.setContentType("application/octet-stream; name=\"" + filename + "\"");
            res.setContentLength(file.length);
            res.setHeader("Content-Disposition", "attachment; filename=\"" + filename + "\";");
            out.write(file);
            out.flush();
            return;
        } else if (req.getParameter("template_delete_cancel") != null) {
            String temp[][] = imcref.sqlQueryMulti("select simple_name,count(meta_id),t.template_id  from templates t left join text_docs td on td.template_id = t.template_id where lang_prefix = ? group by t.template_id,simple_name order by simple_name", new String[]{lang});
            //String temp[] ;
            htmlStr = "";
            Vector vec;
            for (int i = 0; i < temp.length; i++) {
                vec = new Vector();
                vec.add("#template_name#");
                vec.add(temp[i][0]);
                vec.add("#docs#");
                vec.add(temp[i][1]);
                vec.add("#template_id#");
                vec.add(temp[i][2]);
                htmlStr += imcref.parseDoc(vec, "template_list_row.html", lang_prefix);
            }
            vec = new Vector();
            vec.add("#language#");
            vec.add(lang);
            if (temp.length > 0) {
                //					String temps = "" ;
                //					for (int i = 0; i < temp.length; i+=2) {
                //						temps += "<option value=\""+temp[i]+"\">"+temp[i+1]+"</option>" ;
                //					}
                vec.add("#templates#");
                vec.add(htmlStr);
                htmlStr = imcref.parseDoc(vec, "template_delete.html", lang_prefix);
            } else {
                htmlStr = imcref.parseDoc(vec, "template_no_langtemplates.html", lang_prefix);
            }
        } else if (req.getParameter("template_delete") != null) {
            String new_temp_id = req.getParameter("new_template");
            int template_id = Integer.parseInt(req.getParameter("template"));
            if (new_temp_id != null) {
                imcref.sqlUpdateQuery("update text_docs set template_id = ? where template_id = ?", new String[]{new_temp_id, "" + template_id});
            }
            imcref.deleteTemplate(template_id);
            String temp[][] = imcref.sqlQueryMulti("select simple_name,count(meta_id),t.template_id from templates t left join text_docs td on td.template_id = t.template_id where lang_prefix = ? group by t.template_id,simple_name order by simple_name", new String[]{lang});
            htmlStr = "";
            Vector vec;
            for (int i = 0; i < temp.length; i++) {
                vec = new Vector();
                vec.add("#template_name#");
                vec.add(temp[i][0]);
                vec.add("#docs#");
                vec.add(temp[i][1]);
                vec.add("#template_id#");
                vec.add(temp[i][2]);
                htmlStr += imcref.parseDoc(vec, "template_list_row.html", lang_prefix);
            }
            vec = new Vector();
            vec.add("#language#");
            vec.add(lang);
            if (temp.length > 0) {
                vec.add("#templates#");
                vec.add(htmlStr);
            }
            /**/
            htmlStr = imcref.parseDoc(vec, "template_delete.html", lang_prefix);
        } else if (req.getParameter("assign") != null) {
            String grp_id = req.getParameter("group_id");
            String temp_id[] = req.getParameterValues("unassigned");
            if (temp_id == null) {
                htmlStr = parseAssignTemplates(grp_id, lang, lang_prefix, host);
                out.print(htmlStr);
                return;
            }
            for (int i = 0; i < temp_id.length; i++) {
                imcref.sqlUpdateQuery("insert into templates_cref (group_id,template_id) values(?,?)", new String[]{grp_id, temp_id[i]});
            }
            htmlStr = parseAssignTemplates(grp_id, lang, lang_prefix, host);
        } else if (req.getParameter("deassign") != null) {
            String grp_id = req.getParameter("group_id");
            String temp_id[] = req.getParameterValues("assigned");
            if (temp_id == null) {
                htmlStr = parseAssignTemplates(grp_id, lang, lang_prefix, host);
                out.print(htmlStr);
                return;
            }
            for (int i = 0; i < temp_id.length; i++) {
                String sqlStr = "delete from templates_cref where group_id = ? and template_id = ?";
                imcref.sqlUpdateQuery(sqlStr, new String[]{grp_id, temp_id[i]});
            }
            htmlStr = parseAssignTemplates(grp_id, lang, lang_prefix, host);
        } else if (req.getParameter("show_assigned") != null) {
            String grp_id = req.getParameter("templategroup");
            htmlStr = parseAssignTemplates(grp_id, lang, lang_prefix, host);
        } else if (req.getParameter("template_rename") != null) {
            int template_id = Integer.parseInt(req.getParameter("template"));
            String name = req.getParameter("name");
            if (name == null || "".equals(name)) {
                Vector vec = new Vector();
                vec.add("#language#");
                vec.add(lang);
                htmlStr = imcref.parseDoc(vec, "template_rename_name_blank.html", lang_prefix);
            } else {
                String sqlStr = "update templates set simple_name = ? where template_id = ?";
                imcref.sqlUpdateQuery(sqlStr, new String[]{name, "" + template_id});
                String temp[];
                temp = imcref.sqlQuery("select template_id, simple_name from templates where lang_prefix = ? order by simple_name", new String[]{lang});
                Vector vec = new Vector();
                vec.add("#language#");
                vec.add(lang);
                if (temp.length > 0) {
                    String temps = "";
                    for (int i = 0; i < temp.length; i += 2) {
                        temps += "<option value=\"" + temp[i] + "\">" + temp[i + 1] + "</option>";
                    }
                    vec.add("#templates#");
                    vec.add(temps);
                    htmlStr = imcref.parseDoc(vec, "template_rename.html", lang_prefix);
                } else {
                    htmlStr = imcref.parseDoc(vec, "template_no_langtemplates.html", lang_prefix);
                }
            }
        } else if (req.getParameter("template_delete_check") != null) {
            int template_id = Integer.parseInt(req.getParameter("template"));
            String sqlStr = "select top 50 meta_id from text_docs where template_id = " + template_id;
            String temp[] = imcref.sqlQuery(sqlStr, new String[]{"" + template_id});
            if (temp.length > 0) {
                Vector vec = new Vector();
                vec.add("#language#");
                vec.add(lang);
                String tempstr = "";
                for (int i = 0; i < temp.length; i++) {
                    tempstr += "<option>" + temp[i] + "</option>";
                }
                vec.add("#template#");
                vec.add(String.valueOf(template_id));
                vec.add("#docs#");
                vec.add(tempstr);
                temp = imcref.sqlQuery("select t.template_id,t.simple_name from templates t where lang_prefix = ? and template_id != ? order by simple_name", new String[]{lang, "" + template_id});
                tempstr = "";
                for (int i = 0; i < temp.length; i += 2) {
                    tempstr += "<option value=\"" + temp[i] + "\">" + temp[i + 1] + "</option>";
                }
                vec.add("#templates#");
                vec.add(tempstr);

                htmlStr = imcref.parseDoc(vec, "template_delete_warning.html", lang_prefix);
            } else {
                imcref.deleteTemplate(template_id);
                String foo[][] = imcref.sqlQueryMulti("select simple_name,count(meta_id),t.template_id  from templates t left join text_docs td on td.template_id = t.template_id where lang_prefix = ? group by t.template_id,simple_name order by simple_name", new String[]{lang});
                htmlStr = "";
                Vector vec;
                for (int i = 0; i < foo.length; i++) {
                    vec = new Vector();
                    vec.add("#template_name#");
                    vec.add(foo[i][0]);
                    vec.add("#docs#");
                    vec.add(foo[i][1]);
                    vec.add("#template_id#");
                    vec.add(foo[i][2]);
                    htmlStr += imcref.parseDoc(vec, "template_list_row.html", lang_prefix);
                }
                vec = new Vector();
                vec.add("#language#");
                vec.add(lang);
                if (foo.length > 0) {
                    vec.add("#templates#");
                    vec.add(htmlStr);
                }
                /**/
                htmlStr = imcref.parseDoc(vec, "template_delete.html", lang_prefix);
            }
        } else if (req.getParameter("group_delete_check") != null) {
            int grp_id = Integer.parseInt(req.getParameter("templategroup"));
            String sqlStr = "select simple_name from templates t,templates_cref c where c.template_id = t.template_id and group_id = ? order by simple_name";
            String temp[] = imcref.sqlQuery(sqlStr, new String[]{"" + grp_id});
            if (temp.length > 0) {
                String temps = temp[0];
                for (int i = 1; i < temp.length; i++) {
                    temps += ", " + temp[i];
                }
                Vector vec = new Vector();
                vec.add("#templates#");
                vec.add(temps);
                vec.add("#templategroup#");
                vec.add(String.valueOf(grp_id));
                htmlStr = imcref.parseDoc(vec, "templategroup_delete_warning.html", lang_prefix);
            } else {
                imcref.deleteTemplateGroup(grp_id);
                temp = imcref.sqlProcedure("GetTemplategroups", new String[0]);
                String temps = "";
                for (int i = 0; i < temp.length; i += 2) {
                    temps += "<option value=\"" + temp[i] + "\">" + temp[i + 1] + "</option>";
                }
                Vector vec = new Vector();
                vec.add("#templategroups#");
                vec.add(temps);
                htmlStr = imcref.parseDoc(vec, "templategroup_delete.html", lang_prefix);
            }
        } else if (req.getParameter("group_delete") != null) {
            int grp_id = Integer.parseInt(req.getParameter("templategroup"));
            imcref.sqlUpdateQuery("delete from templates_cref where group_id = ?", new String[]{"" + grp_id});
            imcref.deleteTemplateGroup(grp_id);
            String temp[] = imcref.sqlProcedure("GetTemplategroups", new String[0]);
            String temps = "";
            for (int i = 0; i < temp.length; i += 2) {
                temps += "<option value=\"" + temp[i] + "\">" + temp[i + 1] + "</option>";
            }
            Vector vec = new Vector();
            vec.add("#templategroups#");
            vec.add(temps);
            htmlStr = imcref.parseDoc(vec, "templategroup_delete.html", lang_prefix);
        } else if (req.getParameter("group_delete_cancel") != null) {
            String temp[];
            temp = imcref.sqlProcedure("GetTemplategroups", new String[0]);
            String temps = "";
            for (int i = 0; i < temp.length; i += 2) {
                temps += "<option value=\"" + temp[i] + "\">" + temp[i + 1] + "</option>";
            }
            Vector vec = new Vector();
            vec.add("#templategroups#");
            vec.add(temps);
            htmlStr = imcref.parseDoc(vec, "templategroup_delete.html", lang_prefix);
        } else if (req.getParameter("group_add") != null) {
            String name = req.getParameter("name");
            if (name == null || name.equals("")) {
                htmlStr = imcref.parseDoc(null, "templategroup_add_name_blank.html", lang_prefix);
            } else {
                String sqlStr = "select group_id from templategroups where group_name = ?";
                if (imcref.sqlQueryStr(sqlStr, new String[]{name}) != null) {
                    htmlStr = imcref.parseDoc(null, "templategroup_add_exists.html", lang_prefix);
                } else {
                    imcref.sqlUpdateQuery("declare @new_id int\n" +
                            "select @new_id = max(group_id)+1 from templategroups\n" +
                            "insert into templategroups values(@new_id,?)", new String[]{name});
                    htmlStr = imcref.parseDoc(null, "templategroup_add.html", lang_prefix);
                }
            }
        } else if (req.getParameter("group_rename") != null) {
            int grp_id = Integer.parseInt(req.getParameter("templategroup"));
            String name = req.getParameter("name");
            if (name == null || name.equals("")) {
                htmlStr = imcref.parseDoc(null, "templategroup_rename_name_blank.html", lang_prefix);
            } else {
                imcref.changeTemplateGroupName(grp_id, name);
                String temp[];
                temp = imcref.sqlProcedure("GetTemplategroups", new String[0]);
                String temps = "";
                for (int i = 0; i < temp.length; i += 2) {
                    temps += "<option value=\"" + temp[i] + "\">" + temp[i + 1] + "</option>";
                }
                Vector vec = new Vector();
                vec.add("#templategroups#");
                vec.add(temps);
                htmlStr = imcref.parseDoc(vec, "templategroup_rename.html", lang_prefix);
            }
        } else if (req.getParameter("list_templates_docs") != null) {
            String template_id = req.getParameter("template");
            String temp[][] = imcref.sqlQueryMulti("select simple_name,count(meta_id),t.template_id  from templates t left join text_docs td on td.template_id = t.template_id where lang_prefix = ? group by t.template_id,simple_name order by simple_name", new String[]{lang});
            htmlStr = "";
            for (int i = 0; i < temp.length; i++) {
                Vector vec = new Vector();
                vec.add("#template_name#");
                vec.add(temp[i][0]);
                vec.add("#docs#");
                vec.add(temp[i][1]);
                vec.add("#template_id#");
                vec.add(temp[i][2]);
                htmlStr += imcref.parseDoc(vec, "template_list_row.html", lang_prefix);
            }
            Vector vec2 = new Vector();
            vec2.add("#template_list#");
            vec2.add(htmlStr);
            if (template_id != null) {
                temp = imcref.sqlQueryMulti("select td.meta_id, meta_headline from text_docs td join meta m on td.meta_id = m.meta_id where template_id = ? order by td.meta_id", new String[]{template_id});
                String htmlStr2 = "";
                for (int i = 0; i < temp.length; i++) {
                    Vector vec = new Vector();
                    vec.add("#meta_id#");
                    vec.add(temp[i][0]);
                    vec.add("#meta_headline#");
                    String[] pd = {
                        "&", "&amp;",
                        "<", "&lt;",
                        ">", "&gt;",
                        "\"", "&quot;"
                    };
                    if (temp[i][1].length() > 60) {
                        temp[i][1] = temp[i][1].substring(0, 57) + "...";
                    }
                    temp[i][1] = Parser.parseDoc(temp[i][1], pd);
                    vec.add(temp[i][1]);
                    htmlStr2 += imcref.parseDoc(vec, "templates_docs_row.html", lang_prefix);
                }
                vec2.add("#templates_docs#");
                vec2.add(htmlStr2);
            }
            vec2.add("#language#");
            vec2.add(lang);
            htmlStr = imcref.parseDoc(vec2, "template_list.html", lang_prefix);
        } else if (req.getParameter("show_doc") != null) {
            String meta_id = req.getParameter("templates_doc");
            if (meta_id != null) {
                Utility.redirect(req, res, "AdminDoc?meta_id=" + meta_id);
                return;
            }
            String temp[][] = imcref.sqlQueryMulti("select simple_name,count(meta_id),t.template_id from templates t left join text_docs td on td.template_id = t.template_id where lang_prefix = ? group by t.template_id,simple_name order by simple_name", new String[]{lang});
            htmlStr = "";
            for (int i = 0; i < temp.length; i++) {
                Vector vec = new Vector();
                vec.add("#template_name#");
                vec.add(temp[i][0]);
                vec.add("#docs#");
                vec.add(temp[i][1]);
                vec.add("#template_id#");
                vec.add(temp[i][2]);
                htmlStr += imcref.parseDoc(vec, "template_list_row.html", lang_prefix);
            }
            Vector vec = new Vector();
            vec.add("#template_list#");
            vec.add(htmlStr);
            vec.add("#language#");
            vec.add(lang);
            htmlStr = imcref.parseDoc(vec, "template_list.html", lang_prefix);
        }

        out.print(htmlStr);
    }

    private String parseAssignTemplates(String grp_id, String language, String lang_prefix, String host) throws IOException {
        IMCServiceInterface imcref = IMCServiceRMI.getIMCServiceInterfaceByHost(host);
        String temp[];
        temp = imcref.sqlProcedure("GetTemplategroups", new String[0]);
        String temps = "";
        for (int i = 0; i < temp.length; i += 2) {
            if (grp_id.equals(temp[i])) {
                temps += "<option selected value=\"" + temp[i] + "\">" + temp[i + 1] + "</option>";
            } else {
                temps += "<option value=\"" + temp[i] + "\">" + temp[i + 1] + "</option>";
            }
        }
        Vector vec = new Vector();
        vec.add("#templategroups#");
        vec.add(temps);
        temps = "";
        temp = imcref.sqlQuery("select t.template_id,t.simple_name from templates_cref c join templates t on t.template_id = c.template_id where group_id = ? and lang_prefix = ? order by t.simple_name", new String[]{grp_id, language});
        String list[];
        list = imcref.getDemoTemplateList();
        for (int i = 0; i < temp.length; i += 2) {
            int tmp = Integer.parseInt(temp[i]);
            for (int j = 0; j < list.length; j++) {
                try {
                    if (Integer.parseInt(list[j]) == tmp) {
                        temp[i + 1] = "*" + temp[i + 1];
                        break;
                    }
                } catch (NumberFormatException ex) {
                    log.debug("Exception occured" + ex);
                }
            }
            temps += "<option value=\"" + temp[i] + "\">" + temp[i + 1] + "</option>";
        }
        if (grp_id != null) {

            vec.add("#assigned#");
            vec.add(temps);
            temp = imcref.sqlQuery("select t.template_id,t.simple_name from templates t where lang_prefix = ? and t.template_id not in (select template_id from templates_cref where group_id = ?) order by t.simple_name", new String[]{language, grp_id});
            temps = "";
            for (int i = 0; i < temp.length; i += 2) {
                int tmp = Integer.parseInt(temp[i]);
                for (int j = 0; j < list.length; j++) {
                    try {
                        if (Integer.parseInt(list[j]) == tmp) {
                            temp[i + 1] = "*" + temp[i + 1];
                            break;
                        }
                    } catch (NumberFormatException ex) {

                    }
                }
                temps += "<option value=\"" + temp[i] + "\">" + temp[i + 1] + "</option>";
            }
            vec.add("#unassigned#");
            vec.add(temps);
            temps = imcref.sqlQueryStr("select group_name from templategroups where group_id = ?", new String[]{grp_id});
            if (temps == null) {
                temps = "";
            }
            vec.add("#group#");
            vec.add(temps);
            vec.add("#group_id#");
            vec.add(String.valueOf(grp_id));
        }
        vec.add("#language#");
        vec.add(language);
        return imcref.parseDoc(vec, "template_assign.html", lang_prefix);
    }

    public void log(String str) {
        super.log(str);
        System.out.println("TemplateChange: " + str);
    }

}
