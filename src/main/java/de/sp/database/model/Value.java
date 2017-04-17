package de.sp.database.model;


public class Value{

   private Long id;

   private Long valuestore_key;

   private Long school_id;

   private String name;

   private String abbreviation;

   private String external_key;



   public Value(){
}



   public Value(Long id, Long valuestore_key, Long school_id, String name, String abbreviation, String external_key){

      this.id = id;
      this.valuestore_key = valuestore_key;
      this.school_id = school_id;
      this.name = name;
      this.abbreviation = abbreviation;
      this.external_key = external_key;
}

   public Long getId(){

      return id;

   }

   public Long getValuestore_key(){

      return valuestore_key;

   }

   public Long getSchool_id(){

      return school_id;

   }

   public String getName(){

      return name;

   }

   public String getAbbreviation(){

      return abbreviation;

   }

   public String getExternal_key(){

      return external_key;

   }

}