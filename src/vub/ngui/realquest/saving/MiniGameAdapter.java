package vub.ngui.realquest.saving;

import java.lang.reflect.Type;

import vub.ngui.realquest.model.MiniGame;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class MiniGameAdapter implements JsonDeserializer<MiniGame>,
		JsonSerializer<MiniGame> {

		public JsonElement serialize(MiniGame src, Type typeOfSrc, JsonSerializationContext context) {
	        JsonObject result = new JsonObject();
	        result.add("type", new JsonPrimitive(src.getClass().getSimpleName()));
	        result.add("properties", context.serialize(src, src.getClass()));

	        return result;
	    }

	    public MiniGame deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
	        throws JsonParseException {
	        JsonObject jsonObject = json.getAsJsonObject();
	        String type = jsonObject.get("type").getAsString();
	        JsonElement element = jsonObject.get("properties");

	        try {
	            return context.deserialize(element, Class.forName("vub.ngui.realquest.model." + type));
	        } catch (ClassNotFoundException cnfe) {
	            throw new JsonParseException("Unknown element type: " + type, cnfe);
	        }
	    }
	    

}
