package models;


/*
 * Every body can be an Entity*/
public interface AgentEntity {

    //Say who you are
    enumEntity Iam();

    //say your location
    int myXlocation = 0;
    int myYlocation = 0;
}