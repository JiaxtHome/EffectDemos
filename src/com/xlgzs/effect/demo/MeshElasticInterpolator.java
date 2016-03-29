package com.xlgzs.effect.demo;

import android.view.animation.Interpolator;

public class MeshElasticInterpolator implements Interpolator {
    
    /**
     * value is 0
     */
    public static final byte IN = 0;
    /**
     * value is 1
     */
    public static final byte OUT = 1;
    /**
     * value is 2
     */
    public static final byte INOUT = 2;
    
    byte _mode = 0;

    private static final float PI = 3.14159265f;
    
    /**
     * @param mode one of {@link #IN}, {@link #OUT} or {@link #INOUT}
     */
    public MeshElasticInterpolator(byte mode) {
        if (mode > -1 && mode < 3) {
            _mode = mode;
        } else {
            throw new IllegalArgumentException("The mode must be 0, 1 or 2. See the doc");
        }
    }

    @Override
    public float getInterpolation(float input) {
        float a, p, s;
        switch (_mode) {
        case IN:
            a = param_a;
            p = param_p;
            if (input==0) return 0;  if (input==1) return 1; if (!setP) p=.3f;
            if (!setA || a < 1) { a=1; s=p/4; }
            else s = p/(2*PI) * (float)Math.asin(1/a);
            return -(a*(float)Math.pow(2,10*(input-=1)) * (float)Math.sin( (input-s)*(2*PI)/p ));
        case OUT:
            a = param_a;
            p = param_p;
            if (input==0) return 0;  if (input==1) return 1; if (!setP) p=.3f;
            if (!setA || a < 1) { a=1; s=p/4; }
            else s = p/(2*PI) * (float)Math.asin(1/a);
            return a*(float)Math.pow(2,-10*input) * (float)Math.sin( (input-s)*(2*PI)/p ) + 1;
        case INOUT:
            a = param_a;
            p = param_p;
            if (input==0) return 0;  if ((input*=2)==2) return 1; if (!setP) p=.3f*1.5f;
            if (!setA || a < 1) { a=1; s=p/4; }
            else s = p/(2*PI) * (float)Math.asin(1/a);
            if (input < 1) return -.5f*(a*(float)Math.pow(2,10*(input-=1)) * (float)Math.sin( (input-s)*(2*PI)/p ));
            return a*(float)Math.pow(2,-10*(input-=1)) * (float)Math.sin( (input-s)*(2*PI)/p )*.5f + 1;
        }
        return input;
    }

    protected float param_a;
    protected float param_p;
    protected boolean setA = false;
    protected boolean setP = false;

    public MeshElasticInterpolator a(float a) {
        param_a = a;
        this.setA = true;
        return this;
    }

    public MeshElasticInterpolator p(float p) {
        param_p = p;
        this.setP = true;
        return this;
    }

}
