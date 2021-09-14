package com.faceunity.agorawithfaceunity;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.Iterator;

import io.agora.extension.ExtensionManager;

/**
 * @author Qinyu on 2021-03-30
 * @description
 */
public final class FuJsonHelper {
    private static volatile FuJsonHelper instance;
    public static JSONObject dFace, dShape, dBody; //默认值

    private JSONObject face, shape, body;

    static {
        try {
            dFace = new JSONObject();
            JSONObject faceIn = new JSONObject();
            faceIn.put("skin", 70);
            faceIn.put("white", 30);
            faceIn.put("red", 30);
            faceIn.put("lighteye", 0);
            faceIn.put("sharpen", 70);
            faceIn.put("teeth", 0);
            faceIn.put("blackeye", 0);
            faceIn.put("ade", 0);
            dFace.put("beautyface", faceIn);

            dShape = new JSONObject();
            JSONObject shapeIn = new JSONObject();
            shapeIn.put("thinface", 40);
            shapeIn.put("bigeye", 40);
            shapeIn.put("circleeye", 0);
            shapeIn.put("chin", 30);
            shapeIn.put("forehead", 30);
            shapeIn.put("thinnose", 50);
            shapeIn.put("mouth", 40);
            shapeIn.put("vface", 50);
            shapeIn.put("naface", 0);
            shapeIn.put("smallface", 0);
            shapeIn.put("thincheekbone", 0);
            shapeIn.put("thinmandible", 0);
            shapeIn.put("openeye", 0);
            shapeIn.put("eyedis", 50);
            shapeIn.put("eyerid", 50);
            shapeIn.put("longnose", 50);
            shapeIn.put("shrinking", 50);
            shapeIn.put("smile", 0);
            dShape.put("beautyshape", shapeIn);

            dBody = new JSONObject();
            JSONObject bodyIn = new JSONObject();
            bodyIn.put("thinbodyIn", 0);
            bodyIn.put("longleg", 0);
            bodyIn.put("thinwaist", 0);
            bodyIn.put("beashou", 0);
            bodyIn.put("beaass", 0);
            bodyIn.put("smallhead", 0);
            bodyIn.put("thinleg", 0);
            dBody.put("beautybody", bodyIn);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static FuJsonHelper getInstance() {
        if (instance == null) {
            synchronized (FuJsonHelper.class) {
                if (instance == null) {
                    instance = new FuJsonHelper();
                }
            }
        }
        return instance;
    }

    private FuJsonHelper() {
        try {
            face = new JSONObject(dFace.toString());
            shape = new JSONObject(dShape.toString());
            body = new JSONObject(dBody.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void putAndSet(Object value, String key) {
        try {
            JSONObject o = new JSONObject();
            o.put(key, value);
            ExtensionManager.nativeSetParameters(o.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void resetAllParamZero(String type) {
        try {
            Field object = this.getClass().getDeclaredField(type);
            object.setAccessible(true);
            JSONObject out = (JSONObject) object.get(this);
            JSONObject in = out.getJSONObject("beauty" + type);
            for (Iterator<String> it = in.keys(); it.hasNext(); ) {
                String key = it.next();
                in.put(key, 0);
            }
            ExtensionManager.nativeSetParameters(out.toString());
        } catch (NoSuchFieldException | IllegalAccessException | JSONException e) {
            e.printStackTrace();
        }
    }

    public void resetFace() {
        try {
            face = new JSONObject(dFace.toString());
            ExtensionManager.nativeSetParameters(face.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void resetShape() {
        try {
            shape = new JSONObject(dShape.toString());
            ExtensionManager.nativeSetParameters(shape.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 这里会频繁调用所以不反射了, 反射参考 resetAllParamZero,节省很多代码
     */
    public void setBeautyFace(int value, String key) {
        try {
            JSONObject o = face.getJSONObject("beautyface");
            o.put(key, value);
            ExtensionManager.nativeSetParameters(face.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void setBeautyShape(int value, String key) {
        try {
            JSONObject o = shape.getJSONObject("beautyshape");
            o.put(key, value);
            ExtensionManager.nativeSetParameters(shape.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void setBeautyBody(int value, String key) {
        try {
            JSONObject o = body.getJSONObject("beautybody");
            o.put(key, value);
            ExtensionManager.nativeSetParameters(body.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void setParams(String key, Object[] values, String[] params) {
        try {
            JSONObject o = new JSONObject();
            JSONObject in = new JSONObject();
            for (int i = 0; i < params.length; i++) {
                in.put(params[i], values[i]);
            }
            o.put(key, in);
            ExtensionManager.nativeSetParameters(o.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public int getBeautyFaceValue(String key) {
        try {
            JSONObject o = face.getJSONObject("beautyface");
            return o.getInt(key);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public int getBeautyShapeValue(String key) {
        try {
            JSONObject o = shape.getJSONObject("beautyshape");
            return o.getInt(key);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public int getBeautyBodyValue(String key) {
        try {
            JSONObject o = body.getJSONObject("beautybody");
            return o.getInt(key);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return -1;
    }
}
