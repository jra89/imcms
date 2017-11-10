package com.imcode.imcms.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommonContentDTO {

    private Integer id;
    private Integer docId;
    private LanguageDTO language;
    private String headline;
    private String menuText;
    private String menuImageURL;
    private boolean isEnabled;
    private Integer versionNo;

}
