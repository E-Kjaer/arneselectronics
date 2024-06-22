package CMS;

public class ControllerLogic {

    public static double ImageFitsX(double ImageX,double ImageWidth,double BackgroundWidth, double SceneX, double border){


        System.out.println("X: =" + SceneX);
        if(SceneX + border < 0){
            return 0;
        }
        if(SceneX + border + ImageWidth > BackgroundWidth){
            return BackgroundWidth - ImageWidth;
        }
        if(SceneX - border >= 0 && SceneX + ImageWidth <= BackgroundWidth){
            return SceneX + border - ImageWidth;
        }

        return 0;
    }

    public static String clickedFont(String[] fonts, String clickedStyle){
        for(int i = 0; i < fonts.length; i++){
            if(clickedStyle.contains(fonts[i])){
                return fonts[i];
            }
        }
        return "";
    }
}

