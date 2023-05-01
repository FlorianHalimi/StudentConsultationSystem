package StudentConsultationSystem.models;

import StudentConsultationSystem.repositories.TimeConversion;

import java.time.LocalDateTime;


public class Konsultimet {

    private int Konsultimi_id;

    private String professor;
    private String lenda;

    private String student;
    private LocalDateTime fillimi;
    private LocalDateTime  fundi;
    private LocalDateTime data;
    private String email;

        public Konsultimet(String professor, String student, String lenda,LocalDateTime fillimi, LocalDateTime fundi, LocalDateTime data, String email){
        this.professor = professor;
        this.student = student;
        this.lenda = lenda;
        this.fillimi = fillimi;
        this.fundi = fundi;
        this.data = data;
        this.email = email;
    }

    public Konsultimet(String professor, String student, String lenda,int konsultimi_id,LocalDateTime fillimi, LocalDateTime fundi, LocalDateTime data, String email){
        this.professor = professor;
        this.student = student;
        this.lenda = lenda;
        this.Konsultimi_id = konsultimi_id;
        this.fillimi = fillimi;
        this.fundi = fundi;
        this.data = data;
        this.email = email;
    }
    public Konsultimet(){

    }

    public int getKonsultimi_id() {
        return Konsultimi_id;
    }

    public void setKonsultimi_id(int konsultimi_id) {
        Konsultimi_id = konsultimi_id;
    }

    public String getProfessor() {
        return professor;
    }

    public void setProfessor(String professor) {
        this.professor = professor;
    }

    public String getStudent() {
        return student;
    }

    public void setStudent(String student) {
        this.student = student;
    }

    public String getLenda() {
        return lenda;
    }

    public void setLenda(String lenda) {
        this.lenda = lenda;
    }

    public LocalDateTime getFillimi() {
        return fillimi;
    }

    public void setFillimi(LocalDateTime fillimi) {
        this.fillimi = fillimi;
    }

    public LocalDateTime getFundi() {
        return fundi;
    }

    public void setFundi(LocalDateTime fundi) {
        this.fundi = fundi;
    }

    public LocalDateTime getData() {
        return data;
    }

    public void setData(LocalDateTime data) {
        this.data = data;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    public String getStartDateFormatted() {
        return TimeConversion.formatDate(fillimi);
    }
    public String getStartTimeFormatted() {
        return TimeConversion.formatTime(fillimi);
    }
}
