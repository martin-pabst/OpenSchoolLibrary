package de.sp.database.model;


public class Subject{

   private Long id;

   private String shortname;

   private String longname;

   private Long school_id;

   private String key1;

   private String key2;

   private  boolean is_religion;

   private boolean is_language;


   public Subject(){
}



   public Subject(Long id, String shortname, String longname, Long school_id, String key1, String key2,
                  boolean is_religion, boolean is_language){

      this.id = id;
      this.shortname = shortname;
      this.longname = longname;
      this.school_id = school_id;
      this.key1 = key1;
      this.key2 = key2;
      this.is_religion = is_religion;
      this.is_language = is_language;
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

   public boolean isIs_religion() {
      return is_religion;
   }

   public boolean isIs_language() {
      return is_language;
   }
}