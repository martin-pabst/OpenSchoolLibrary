package de.sp.database.model;


public class Subject{

   private Long id;

   private String shortname;

   private String longname;

   private Long school_id;

   private String key1;

   private String key2;



   public Subject(){
}



   public Subject(Long id, String shortname, String longname, Long school_id, String key1, String key2){

      this.id = id;
      this.shortname = shortname;
      this.longname = longname;
      this.school_id = school_id;
      this.key1 = key1;
      this.key2 = key2;
}

   public Long getId(){

      return id;

   }

   public String getShortname(){

      return shortname;

   }

   public String getLongname(){

      return longname;

   }

   public Long getSchool_id(){

      return school_id;

   }

   public String getKey1(){

      return key1;

   }

   public String getKey2(){

      return key2;

   }

}