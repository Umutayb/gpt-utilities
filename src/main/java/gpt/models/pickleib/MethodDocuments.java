package gpt.models.pickleib;

import lombok.Data;

import java.util.List;

@Data
public class MethodDocuments {
    List<MethodResponseModel> documents;
}
