package de.sp.database.model;


public class BookForm{

   private Long id;

   private Long book_id;

   private Long form_id;

   private Long curriculum_id; // references value
   
   private Integer languageyear;


   public BookForm(){
}




   public BookForm(Long id, Long book_id, Long form_id, Long curriculum_id,
		Integer languageyear) {
	super();
	this.id = id;
	this.book_id = book_id;
	this.form_id = form_id;
	this.curriculum_id = curriculum_id;
	this.languageyear = languageyear;
}




public Long getId(){

      return id;

   }

   public Long getBook_id(){

      return book_id;

   }

   public Long getForm_id(){

      return form_id;

   }



public Long getCurriculum_id() {
	return curriculum_id;
}



public Integer getLanguageyear() {
	return languageyear;
}
   
   

}