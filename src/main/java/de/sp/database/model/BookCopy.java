package de.sp.database.model;


import java.util.Date;

public class BookCopy{

   private Long id;

   private Long book_id;

   private String edition;

   private String barcode;

   private Date purchase_date;

   private Date sorted_out_date;


   public BookCopy(){
}



   public BookCopy(Long id, Long book_id, String edition, String barcode, Date purchase_date, Date sorted_out_date){

      this.id = id;
      this.book_id = book_id;
      this.edition = edition;
      this.barcode = barcode;
      this.purchase_date = purchase_date;
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

   public Date getPurchase_date() {
      return purchase_date;
   }

   public Date getSorted_out_date() {
      return sorted_out_date;
   }
}