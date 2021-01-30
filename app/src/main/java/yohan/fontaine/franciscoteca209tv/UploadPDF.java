package yohan.fontaine.franciscoteca209tv;

public class UploadPDF {
    private String imageUrl;
    private String pdfUrl;
    private String title;
    private String author;

    // Default constructor required for calls to
    // DataSnapshot.getValue(User.class)
    public UploadPDF() {
    }

    public UploadPDF(String imageUrl, String pdfUrl, String title, String author) {
        this.imageUrl = imageUrl;
        this.pdfUrl = pdfUrl;
        this.title = title;
        this.author = author;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getPdfUrl() {
        return pdfUrl;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }
}
