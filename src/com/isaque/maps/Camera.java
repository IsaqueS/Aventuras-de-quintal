package com.isaque.maps;

public class Camera {
    private static int x = 0,y = 0;
    
    public static int getX(){
        return Camera.x;
    }
    
    public static int getY(){
        return Camera.y;
    }
    
    public static void setX(int x){
        Camera.x = x;
    }
    
    public static void setY(int y){
        Camera.y = y;
    }
    
    public static int clamp(int current, int min, int max){
        if (current < min) current = min;
        if (current > max) current = max;
        return current;
    }
}
