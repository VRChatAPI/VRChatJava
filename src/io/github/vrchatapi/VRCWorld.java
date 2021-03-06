package io.github.vrchatapi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONObject;

public class VRCWorld {
	
	protected String id;
	protected String name;
	protected String imageUrl;
	protected String authorName;
	protected String authorId;
	protected String assetUrl;
	protected String description;
	protected List<String> tags = new ArrayList<>();
	protected int version;
	protected String pluginUrl;
	protected String unityPackageUrl;
	protected ReleaseStatus releaseStatus;
	protected int capacity;
	protected int occupants;
	protected List<VRCWorldInstance> instances = new ArrayList<>();
	protected String thumbnailImageUrl;
	protected boolean curated;
	
	protected VRCWorld() {}
	
	public static VRCWorld fetch(String id) {
		JSONObject obj = ApiModel.sendGetRequest("worlds/" + id, null);
		VRCWorld world = new VRCWorld();
		world.init(obj);
		return world;
	}
	
	public static List<VRCWorld> list(int offset, int count, boolean activeOnly) {
		String endpoint = activeOnly ? "worlds/active" : "worlds";
		HashMap<String, Object> values = new HashMap<>();
		values.put("n", count);
		values.put("offset", offset);
		List<VRCWorld> worlds = new ArrayList<>();
		ApiModel.sendGetRequestArray(endpoint, values).forEach((o) -> {
			if(o instanceof JSONObject) {
				JSONObject obj = (JSONObject)o;
				VRCWorld world = new VRCWorld();
				world.initBrief(obj);
				worlds.add(world);
			}
		});
		return worlds;
	}
	
	public void initBrief(JSONObject json) {
		this.id = json.getString("id");
		this.name = json.getString("name");
		this.imageUrl = json.getString("imageUrl");
		this.thumbnailImageUrl = json.optString("thumbnailImageUrl", "");
		this.authorName = json.getString("authorName");
		this.releaseStatus = ReleaseStatus.valueOf(json.getString("releaseStatus").toUpperCase());
		this.capacity = json.getInt("capacity");
		this.occupants = json.optInt("occupants", 0);
	}
	
	public void init(JSONObject json) {
		this.id = json.getString("id");
		this.name = json.getString("name");
		this.imageUrl = json.getString("imageUrl");
		this.thumbnailImageUrl = json.optString("thumbnailImageUrl", "");
		this.authorName = json.getString("authorName");
		this.authorId = json.getString("authorId");
		this.assetUrl = json.getString("assetUrl");
		this.description = json.getString("description");
		json.getJSONArray("tags").forEach((o) -> tags.add(o.toString()));
		this.version = json.getInt("version");
		this.pluginUrl = json.getString("pluginUrl");
		this.unityPackageUrl = json.optString("unityPackageUrl", "");
		if(json.has("instances")) {
			json.getJSONArray("instances").forEach((o) -> {
				if(o instanceof JSONObject) {
					JSONObject obj = (JSONObject) o;
					VRCWorldInstance instance = new VRCWorldInstance();
					instance.init(obj);
					instances.add(instance);
				}
			});
		}
		this.releaseStatus = ReleaseStatus.valueOf(json.getString("releaseStatus").toUpperCase());
		this.capacity = json.getInt("capacity");
		this.occupants = json.optInt("occupants", 0);
		this.version = json.optInt("assetVersion", Integer.parseInt(json.optString("assetVersion", "0")));
	}

	public VRCWorld getFull() {
		return getFull(false);
	}
	
	public VRCWorld getFull(boolean newInstance) {
		VRCWorld world = newInstance ? new VRCWorld() : this;
		JSONObject obj = ApiModel.sendGetRequest("worlds/" + id, null);
		world.init(obj);
		return world;
	}
	
	public void delete() {
		ApiModel.sendRequest("worlds/" + id, "DELETE", null);
	}
	
	public String getId() {
		return id;
	}
	
	public String getName() {
		return name;
	}
	
	public String getImageUrl() {
		return imageUrl;
	}
	
	public String getAuthorName() {
		return authorName;
	}
	
	public VRCUser getAuthor() {
		return VRCUser.fetch(authorId);
	}
	
	public String getAssetUrl() {
		return assetUrl;
	}
	
	public int getCapacity() {
		return capacity;
	}
	
	public String getDescription() {
		return description;
	}
	
	public List<VRCWorldInstance> getInstances() {
		return instances;
	}
	
	public int getOccupants() {
		return occupants;
	}
	
	public String getPluginUrl() {
		return pluginUrl;
	}
	
	public ReleaseStatus getReleaseStatus() {
		return releaseStatus;
	}
	
	public List<String> getTags() {
		return tags;
	}
	
	public String getThumbnailImageUrl() {
		return thumbnailImageUrl;
	}
	
	public String getUnityPackageUrl() {
		return unityPackageUrl;
	}
	
	public int getVersion() {
		return version;
	}

	@Override
	public String toString() {
		return "VRCWorld [name=" + name + ", imageUrl=" + imageUrl + ", authorName=" + authorName + ", assetUrl="
				+ assetUrl + ", description=" + description + ", tags=" + tags + ", version=" + version + ", pluginUrl="
				+ pluginUrl + ", unityPackageUrl=" + unityPackageUrl + ", releaseStatus=" + releaseStatus
				+ ", capacity=" + capacity + ", occupants=" + occupants + ", instances=" + instances
				+ ", thumbnailImageUrl=" + thumbnailImageUrl + "]";
	}
	
}
