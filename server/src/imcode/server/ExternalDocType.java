package imcode.server ;

public class ExternalDocType implements java.io.Serializable {
	private int    m_DocType ;
	private String m_CallServlet ;
	private String m_DocName ;
	private String m_ParamStr ;

	public ExternalDocType(int DocType,String CallServet,String DocName,String ParamStr) {
		m_DocType     = DocType ;
		m_CallServlet = CallServet ;
		m_DocName     = DocName ;
		m_ParamStr    = ParamStr ;
	}


	public int getDocType() {
		return m_DocType ;
	}


	public String getCallServlet() {
		return m_CallServlet ;
	}

	public String getDocName() {
		return m_DocName ;
	}

	public String getParamStr() {
		return m_ParamStr ;
	}
}
