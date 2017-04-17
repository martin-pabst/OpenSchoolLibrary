package de.sp.database.model;


public class Contact{

   private Long id;

   private String text;

   private String name;

   private String remark;

   private Integer order_number;

   private Long contact_type_id;

   private Long person_id;

   private Long student_id;

   private Long teacher_id;



   public Contact(){
}



   public Contact(Long id, String text, String name, String remark, Integer order_number, Long contact_type_id, Long person_id, Long student_id, Long teacher_id){

      this.id = id;
      this.text = text;
      this.name = name;
      this.remark = remark;
      this.order_number = order_number;
      this.contact_type_id = contact_type_id;
      this.person_id = person_id;
      this.student_id = student_id;
      this.teacher_id = teacher_id;
}

   public Long getId(){

      return id;

   }

   public String getText(){

      return text;

   }

   public String getName(){

      return name;

   }

   public String getRemark(){

      return remark;

   }

   public Integer getOrder_number(){

      return order_number;

   }

   public Long getContact_type_id(){

      return contact_type_id;

   }

   public Long getPerson_id(){

      return person_id;

   }

   public Long getStudent_id(){

      return student_id;

   }

   public Long getTeacher_id(){

      return teacher_id;

   }

}