package com.scsvn.whc_2016;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.scsvn.whc_2016.main.detailphieu.DetailPhieuActivityNoEMDK;
import com.scsvn.whc_2016.main.detailphieu.DetailPhieuInfo;
import com.scsvn.whc_2016.main.detailphieu.Item;

import org.junit.Test;

import java.util.LinkedHashMap;
import java.util.List;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;


/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class DetailPhieuTest {

    @Test
    public void groupPalletId() throws Exception {
        String json = "[\n" +
                "    {\n" +
                "        \"PalletID\": \"8584490\",\n" +
                "        \"ProductNumber\": \"PVM-857668\",\n" +
                "        \"ProductName\": \"MENTOS  857668~\",\n" +
                "        \"Result\": \" \",\n" +
                "        \"QuantityOfPackages\": \"65\"\n" +
                "    },\n" +
                "    {\n" +
                "        \"PalletID\": \"8698032\",\n" +
                "        \"ProductNumber\": \"PVM-857668\",\n" +
                "        \"ProductName\": \"MENTOS  857668~\",\n" +
                "        \"Result\": \" \",\n" +
                "        \"QuantityOfPackages\": \"136\"\n" +
                "    },\n" +
                "    {\n" +
                "        \"PalletID\": \"8675346\",\n" +
                "        \"ProductNumber\": \"PVM-857652\",\n" +
                "        \"ProductName\": \"MENTOS  857652~\",\n" +
                "        \"Result\": \" \",\n" +
                "        \"QuantityOfPackages\": \"136\"\n" +
                "    },\n" +
                "    {\n" +
                "        \"PalletID\": \"8675347\",\n" +
                "        \"ProductNumber\": \"PVM-857652\",\n" +
                "        \"ProductName\": \"MENTOS  857652~\",\n" +
                "        \"Result\": \" \",\n" +
                "        \"QuantityOfPackages\": \"136\"\n" +
                "    },\n" +
                "    {\n" +
                "        \"PalletID\": \"8675348\",\n" +
                "        \"ProductNumber\": \"PVM-857652\",\n" +
                "        \"ProductName\": \"MENTOS  857652~\",\n" +
                "        \"Result\": \" \",\n" +
                "        \"QuantityOfPackages\": \"93\"\n" +
                "    }\n" +
                "]";
        List<DetailPhieuInfo> body = new Gson().fromJson(json, new TypeToken<List<DetailPhieuInfo>>() {
        }.getType());
        DetailPhieuActivityNoEMDK activityNoEMDK = new DetailPhieuActivityNoEMDK();
        LinkedHashMap<String, List<Item>> group = activityNoEMDK.group("asdf", body);
        assertEquals(1, group.size());
        assertArrayEquals(new String[]{"asdf"}, group.keySet().toArray());
        assertEquals("", new Gson().toJson(group));
        assertEquals(3, group.get("MENTOS  857668~").size());
        assertEquals(4, group.get("MENTOS  857652~").size());
    }
}