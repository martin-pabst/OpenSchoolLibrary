package de.sp.database.model;
import java.util.Date;


public class BookCopyStatus{

   private Long id;

   private Long book_copy_id;

   private Date statusdate;

   private String evidence;

   private String username;

   private String borrowername;

   private String mark;

   private String picture_filenames;
   
   private String event;



   public BookCopyStatus(){
}




   public BookCopyStatus(Long id, Long book_copy_id, Date statusdate,
		String evidence, String username, String borrowername, String mark,
		String picture_filenames, String event) {
	super();
	this.id = id;
	this.book_copy_id = book_copy_id;
	this.statusdate = statusdate;
	this.evidence = evidence;
	this.username = username;
	this.borrowername = borrowername;
	this.mark = mark;
	this.picture_filenames = picture_filenames;
	this.event = event;
}




public Long getId(){

      return id;

   }

   public Long getBook_copy_id(){

      return book_copy_id;

   }

   public Date getStatusdate(){

      return statusdate;

   }

   public String getEvidence(){

      return evidence;

   }

   public String getUsername(){

      return username;

   }

   public String getBorrowername(){

      return borrowername;

   }

   public String getMark(){

      return mark;

   }

   public String getPicture_filenames(){

      return picture_filenames;

   }




public String getEvent() {
	return event;
}
   
   

}