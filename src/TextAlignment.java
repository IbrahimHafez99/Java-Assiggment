import helper.FileUtil;

public class TextAlignment {
  public static void main(String[] args) {
    String filename = args[0], alignmentType = args[1], lineLength = args[2];
    String[] paragraphs = FileUtil.readFile((filename));
    for (int i = 0; i < paragraphs.length; i++) {
      System.out.println(paragraphs[i]);
    }
  }
}
