package de.sp.database.model;


import java.util.Date;

public class BookCopy{

   private Long id;

   private Long book_id;

   private String edition;

   private String barcode;

   private Date sorted_out_date;


   public BookCopy(){
}



   public BookCopy(Long id, Long book_id, String edition, String barcode, Date sorted_out_date){

      this.id = id;
      this.book_id = book_id;
      this.edition = edition;
      this.barcode = barcode;
      this.sorted_out_date = sorted_out_date;
}

   public Long getId(){

      return id;

   }

   public Long getBook_id(){

      return book_id;

   }

   public String getEdition(){

      return edition;

   }

   public String getBarcode(){

      return barcode;

   }

   public Date getSorted_out_date() {
      return sorted_out_date;
   }
}