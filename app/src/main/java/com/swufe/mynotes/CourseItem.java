package com.swufe.mynotes;

public class CourseItem {
    private int id;
    private String Course;
    private String Content;


    public CourseItem() {
        super();
        Course = "";
        }
    public CourseItem(String Course, String Content) {
        super();
        this.Course = Course;
        this.Content = Content;

    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getCourse() {
        return Course;
    }
    public void setCourse(String Course) {
        this.Course = Course;
    }
    public String getContent() {
        return Content;
    }
    public void setContent(String Content) {
        this.Content = Content;
    }

}
