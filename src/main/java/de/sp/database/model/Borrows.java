package de.sp.database.model;
import java.util.Date;


public class Borrows{

   private Long id;

   private Long book_copy_id;

   private Long student_id;

   private Long teacher_id;

   private Date begindate;

   private Date enddate;

   private Date return_date;

   private String remarks;



   public Borrows(){
}



   public Borrows(Long id, Long book_copy_id, Long student_id, Long teacher_id, Date begindate, Date enddate, Date return_date, String remarks){

      this.id = id;
      this.book_copy_id = book_copy_id;
      this.student_id = student_id;
      this.teacher_id = teacher_id;
      this.begindate = begindate;
      this.enddate = enddate;
      this.return_date = return_date;
      this.remarks = remarks;
}

   public Long getId(){

      return id;

   }

   public Long getBook_copy_id(){

      return book_copy_id;

   }

   public Long getStudent_id(){

      return student_id;

   }

   public Long getTeacher_id(){

      return teacher_id;

   }

   public Date getBegindate(){

      return begindate;

   }

   public Date getEnddate(){

      return enddate;

   }

   public Date getReturn_date(){

      return return_date;

   }

   public String getRemarks(){

      return remarks;

   }

}