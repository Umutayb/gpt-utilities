package gpt.utilities;

import gpt.api.GPT;

public class Support {
    public static GPT gpt = new GPT("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");
    public static NameGenerator generator = new NameGenerator(gpt);
    public static void main(String[] args) {
        System.out.println(generator.generateName("src/main/java/gpt/utilities/sample.json", "https://demoqa.com/"));
    }

}
