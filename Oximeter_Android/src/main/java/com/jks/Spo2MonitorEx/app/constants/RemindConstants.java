package com.jks.Spo2MonitorEx.app.constants;

public interface RemindConstants {

    interface Key {
        String SP_H_VALUE = "sp_h_value";
        String SP_H_FLAG = "sp_h_flag";
        String SP_L_VALUE = "sp_l_value";
        String SP_L_FLAG = "sp_l_flag";
        String PR_H_VALUE = "pr_h_value";
        String PR_H_FLAG = "pr_h_flag";
        String PR_L_VALUE = "pr_l_value";
        String PR_L_FLAG = "pr_l_flag";
        String PI_H_VALUE = "pi_h_value";
        String PI_H_FLAG = "pi_h_flag";
        String PI_L_VALUE = "pi_l_value";
        String PI_L_FLAG = "pi_l_flag";
    }

    interface Value {
        String OPEN = "open";
        String CLOSE = "close";
        int SP_VALUE_MIN = 50;
        int SP_VALUE_MAX = 100;
        String SP_H_VALUE_DEFAULT = String.valueOf(SP_VALUE_MAX);
        String SP_H_FLAG_DEFAULT = OPEN;
        String SP_L_VALUE_DEFAULT = String.valueOf(SP_VALUE_MIN);
        String SP_L_FLAG_DEFAULT = OPEN;
        int PR_VALUE_MIN = 40;
        int PR_VALUE_MAX = 200;
        String PR_H_VALUE_DEFAULT = String.valueOf(PR_VALUE_MAX);
        String PR_H_FLAG_DEFAULT = OPEN;
        String PR_L_VALUE_DEFAULT = String.valueOf(PR_VALUE_MIN);
        String PR_L_FLAG_DEFAULT = OPEN;
        int PI_VALUE_MIN = 0;
        int PI_VALUE_MAX = 20;
        String PI_H_VALUE_DEFAULT = String.valueOf(PI_VALUE_MAX);
        String PI_H_FLAG_DEFAULT = CLOSE;
        String PI_L_VALUE_DEFAULT = String.valueOf(PI_VALUE_MIN);
        String PI_L_FLAG_DEFAULT = CLOSE;
    }
}
