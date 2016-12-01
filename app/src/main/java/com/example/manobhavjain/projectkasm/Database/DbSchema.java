package com.example.manobhavjain.projectkasm.Database;

/**
 * Created by Manobhav Jain on 8/11/2016.
 */
public class DbSchema {
    public static final class CardsTable{
        public static final String NAME="cardsTable";
        public static final class Cols{
            public static final String UUID="uuid";
            public static final String JSONSTRING="jsonstring";
            public static final String INDIVIDUAL="individual";
            public static final String BACKCOLOR="backcolor";
            public static final String CHANGE="change";




        }
    }
    public static final class ProjectTable{
        public static final String NAME="ProjectTable";
        public static final class Cols{
            public static final String PROJUUID="proj_uuid";
            public static final String PROJTITLE="proj_title";
            public static final String PROJJSONSTRING="projjsonstring";
        }
    }
}
