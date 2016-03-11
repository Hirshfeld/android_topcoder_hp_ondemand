package com.topcoder.hp.idol.ondemand.RestEntites;

import com.google.gson.annotations.Expose;

public class DeleteTextIndexResponse {

    @Expose
    public boolean deleted;
    @Expose
    public String confirm = null;

    @Expose
    public String index = null;


    @Override
    public String toString() {
        return (deleted || confirm == null) ? "not deleted" : "deleted. confirmation code: " + confirm;
    }

}
