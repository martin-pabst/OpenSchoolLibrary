package de.sp.database.model;
import java.util.Date;


public class Fee{

   private Long id;

   private Long borrows_id;

   private double amount;

   private String remarks;

   private Date paid_date;



   public Fee(){
}



   public Fee(Long id, Long borrows_id, double amount, String remarks, Date paid_date){

      this.id = id;
      this.borrows_id = borrows_id;
      this.amount = amount;
      this.remarks = remarks;
      this.paid_date = paid_date;
}

   public Long getId(){

      return id;

   }

   public Long getBorrows_id(){

      return borrows_id;

   }

   public double getAmount(){

      return amount;

   }

   public String getRemarks(){

      return remarks;

   }

   public Date getPaid_date(){

      return paid_date;

   }

}