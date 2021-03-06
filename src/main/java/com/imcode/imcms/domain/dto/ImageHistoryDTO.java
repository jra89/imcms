package com.imcode.imcms.domain.dto;

import com.imcode.imcms.persistence.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class ImageHistoryDTO extends ImageDTO {
    private User modifiedBy;
    private LocalDateTime modifiedAt;
}
