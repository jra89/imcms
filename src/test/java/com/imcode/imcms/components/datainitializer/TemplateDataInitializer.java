package com.imcode.imcms.components.datainitializer;

import com.imcode.imcms.domain.dto.TemplateDTO;
import com.imcode.imcms.domain.dto.TemplateGroupDTO;
import com.imcode.imcms.model.Template;
import com.imcode.imcms.model.TemplateGroup;
import com.imcode.imcms.persistence.entity.TemplateGroupJPA;
import com.imcode.imcms.persistence.entity.TemplateJPA;
import com.imcode.imcms.persistence.entity.TextDocumentTemplateJPA;
import com.imcode.imcms.persistence.repository.TemplateGroupRepository;
import com.imcode.imcms.persistence.repository.TemplateRepository;
import com.imcode.imcms.persistence.repository.TextDocumentTemplateRepository;
import com.imcode.imcms.util.Value;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component
public class TemplateDataInitializer extends TestDataCleaner {

    private final TextDocumentTemplateRepository textDocumentTemplateRepository;
    private final TemplateRepository templateRepository;
    private final TemplateGroupRepository templateGroupRepository;

    public TemplateDataInitializer(TextDocumentTemplateRepository textDocumentTemplateRepository,
                                   TemplateRepository templateRepository,
                                   TemplateGroupRepository templateGroupRepository) {

        super(templateRepository, textDocumentTemplateRepository);
        this.textDocumentTemplateRepository = textDocumentTemplateRepository;
        this.templateRepository = templateRepository;
        this.templateGroupRepository = templateGroupRepository;
    }

    public List<Template> createData(Integer howMuch) {
        return IntStream.range(0, howMuch)
                .mapToObj(i -> Value.with(new TemplateJPA(), template -> {
                    template.setName("template" + i);
                    template.setHidden(Math.random() < 0.5);
                }))
                .map(templateRepository::saveAndFlush)
                .map(TemplateDTO::new)
                .collect(Collectors.toList());
    }

    public Template createData(final String name) {
        return Value.apply(new TemplateJPA(), template -> {
            template.setName(name);
            template.setHidden(Math.random() < 0.5);

            templateRepository.saveAndFlush(template);
            return new TemplateDTO(template);
        });
    }

    public TextDocumentTemplateJPA createData(int docId, String templateName, String childrenTemplate) {
        createData(templateName);
        createData(childrenTemplate);

        final TextDocumentTemplateJPA textDocumentTemplate = new TextDocumentTemplateJPA();
        textDocumentTemplate.setTemplateName(templateName);
        textDocumentTemplate.setChildrenTemplateName(childrenTemplate);
        textDocumentTemplate.setDocId(docId);

        return textDocumentTemplateRepository.save(textDocumentTemplate);
    }

    public TemplateGroup createData(String name, int howMuchContainsTemplates, boolean withoutSaving) {
        final TemplateGroupJPA templateGroupJPA = Value.apply(new TemplateGroupJPA(), templateGroupJpa -> {
            final List<Template> templates = createData(howMuchContainsTemplates);
            templateGroupJpa.setName(name);
            templateGroupJpa.setTemplates(templates.stream().map(TemplateJPA::new).collect(Collectors.toList()));
            if (withoutSaving) {
                return templateGroupJpa;
            }
            return templateGroupRepository.saveAndFlush(templateGroupJpa);
        });
        return new TemplateGroupDTO(templateGroupJPA);
    }


}