import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.jsfml.graphics.ConstTexture;
import org.jsfml.graphics.FloatRect;
import org.jsfml.graphics.Image;
import org.jsfml.graphics.IntRect;
import org.jsfml.graphics.Sprite;
import org.jsfml.system.Vector2f;


public class BitMaskCollisionController {
	
	FloatRect latestIntersect;
	// LatestIntersect is used to know where collision took place
	public FloatRect getLatestIntersect() {
		return latestIntersect;
	}

	BitmaskManager Bitmasks = new BitmaskManager();
	private final int ALPHA_LIMIT = 1; // Upper transparency Limit to avoid collision
	
	public Boolean pixelPerfectTest(GameObject ob1, GameObject ob2) {
		FloatRect intersect;
		Sprite Object1 = ob1.getSprite();
		Sprite Object2 = ob2.getSprite();
		
		intersect = Object1.getGlobalBounds().intersection(Object2.getGlobalBounds());
		if ((intersect != null)) {
			latestIntersect = intersect;
			IntRect O1SubRect = Object1.getTextureRect();
			IntRect O2SubRect = Object2.getTextureRect();

			ArrayList<Integer> mask1 = Bitmasks.GetMask(ob1.getHitBoxTexture());
			ArrayList<Integer> mask2 = Bitmasks.GetMask(ob2.getHitBoxTexture());
			

			// Loop through pixels in the intersection
			for (float i = intersect.left; i < intersect.left+intersect.width; i++) {
				for (float j = intersect.top; j < intersect.top+intersect.height; j++) {
 
					Vector2f o1v = Object1.getInverseTransform().transformPoint(i, j);
					Vector2f o2v = Object2.getInverseTransform().transformPoint(i, j);
 
					// Make sure pixels fall within the sprite's subrect
					if (o1v.x > 0 && o1v.y > 0 && o2v.x > 0 && o2v.y > 0 &&
						o1v.x < O1SubRect.width && o1v.y < O1SubRect.height &&
						o2v.x < O2SubRect.width && o2v.y < O2SubRect.height) {
						// Texture Pixel Perfect
							//if (Bitmasks.GetPixel(mask1, Object1.getTexture(), (int)(o1v.x)+O1SubRect.left, (int)(o1v.y)+O1SubRect.top) > ALPHA_LIMIT &&
								//Bitmasks.GetPixel(mask2, Object2.getTexture(), (int)(o2v.x)+O2SubRect.left, (int)(o2v.y)+O2SubRect.top) > ALPHA_LIMIT)
								//return true;
						// Hit-Box Pixel Perfect
							if (Bitmasks.GetPixel(mask1, ob1.getHitBoxTexture(), (int)(o1v.x)+O1SubRect.left, (int)(o1v.y)+O1SubRect.top) > ALPHA_LIMIT &&
								Bitmasks.GetPixel(mask2, ob2.getHitBoxTexture(), (int)(o2v.x)+O2SubRect.left, (int)(o2v.y)+O2SubRect.top) > ALPHA_LIMIT)
								return true;
					}
				}
			}
		}
		return false;
		
	}
	
	Boolean CircleTest(GameObject Object1, GameObject Object2) {
		IntRect Obj1Size = Object1.getSprite().getTextureRect();
		IntRect Obj2Size = Object2.getSprite().getTextureRect();
		float Radius1 = (Obj1Size.width + Obj1Size.height) / 4;
		float Radius2 = (Obj2Size.width + Obj2Size.height) / 4;
		float DistanceX = Object1.getSprite().getPosition().x - Object2.getSprite().getPosition().x;
		float DistanceY = Object1.getSprite().getPosition().y - Object2.getSprite().getPosition().y;
		
		if((DistanceX * DistanceX + DistanceY * DistanceY <= (Radius1 + Radius2) * (Radius1 + Radius2)))
			return true;
		return false;
	}
	

	class BitmaskManager {
		public BitmaskManager() {
		}


		int GetPixel(ArrayList<Integer> mask, ConstTexture tex, int x, int y) {
			if (x > tex.getSize().x || y > tex.getSize().y)
				return 0;
			//if (mask.size() >= (x + y * tex.getSize().x)) {
			try {
				int pixel = mask.get(x + y * tex.getSize().x);
				return pixel;
			} catch (IndexOutOfBoundsException e) {
				for(int i = 0; i < 100; i++)
					System.out.println("Collision Exception: INDEX OUT OF BOUNDS");
				return 0;
			}
		}

		ArrayList<Integer> GetMask(ConstTexture texture) {
			ArrayList<Integer> mask;
			// Search for texture in Bitmasks array. If not found, create a new bitmask
			for (Map.Entry<ConstTexture, ArrayList<Integer>> mapEntry : Bitmasks.entrySet()) {
				if(mapEntry.getKey().equals(texture)) { // If the texture has been stored before...
					return mapEntry.getValue(); // Return existing bitmask in Bitmasks
				}
			}
			Image img = texture.copyToImage();
			mask = CreateMask(texture, img);
			return mask;
		}

		ArrayList<Integer> CreateMask(ConstTexture tex, Image img) {
			ArrayList<Integer> mask = new ArrayList<Integer>();
			for (int y = 0; y < tex.getSize().y; y++) {
				for (int x = 0; x < tex.getSize().x; x++) {
					System.out.print(x+y*tex.getSize().x);
					mask.add(img.getPixel(x,y).a);
				}
			}
			// Before returning, save created bitmask using texture as key
			// TODO: load textures beforehand, loading screen style
			// TODO: cool idea: write object to file!
			Bitmasks.put(tex, mask);
			return mask;
		}
	
		private Map<ConstTexture, ArrayList<Integer>> Bitmasks = new HashMap<>();

		public Map<ConstTexture, ArrayList<Integer>> getBitmasks() {
			return Bitmasks;
		}
		public void setBitmasks(Map<ConstTexture, ArrayList<Integer>> bitmasks) {
			Bitmasks = bitmasks;
		}
	}
	
}

