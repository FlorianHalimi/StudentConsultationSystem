package StudentConsultationSystem.utils;

import java.util.Properties;

public class AppConfig {
    private static AppConfig instance;
    private Properties props;
    private AppConfig(){
        try{
            props = new Properties();
            props.load(getClass().getResourceAsStream("../resources/config.properties"));
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    public static AppConfig get(){
        if(instance == null){
            instance = new AppConfig();
        }
        return instance;
    }

    public String getAppName(){
        return props.getProperty("appname", "SIMK");
    }
    public String getConnectionString(){
        return props.getProperty("connectionString");
    }
    public String getDriverType(){
        return props.getProperty("driverType");
    }
}
