import java.util.HashMap;
import java.util.List;
import java.util.LinkedList;
import java.util.Map;

import processing.core.PImage;
import processing.core.PApplet;

public final class ImageStore
{
    private Map<String, List<PImage>> images;
    private List<PImage> defaultImages;

    private final int KEYED_IMAGE_MIN = 5;
    private final int KEYED_RED_IDX = 2;
    private final int KEYED_GREEN_IDX = 3;
    private final int KEYED_BLUE_IDX = 4;

    public ImageStore(PImage defaultImage) {
        this.images = new HashMap<>();
        defaultImages = new LinkedList<>();
        defaultImages.add(defaultImage);
    }
    
    public Map<String, List<PImage>> getImages() { return this.images;}
    public List<PImage> getDefaultImages() { return this.defaultImages;}
    
    public void processImageLine(
        Map<String, List<PImage>> images, String line, PApplet screen)
    {
        String[] attrs = line.split("\\s");
        if (attrs.length >= 2) {
            String key = attrs[0];
            PImage img = screen.loadImage(attrs[1]);
            if (img != null && img.width != -1) {
                List<PImage> imgs = getImages(images, key);
                imgs.add(img);

                if (attrs.length >= KEYED_IMAGE_MIN) {
                    int r = Integer.parseInt(attrs[KEYED_RED_IDX]);
                    int g = Integer.parseInt(attrs[KEYED_GREEN_IDX]);
                    int b = Integer.parseInt(attrs[KEYED_BLUE_IDX]);
                    Factory.setAlpha(img, screen.color(r, g, b), 0);
                }
            }
        }
    }

    public List<PImage> getImageList(String key) {
        return this.images.getOrDefault(key, this.defaultImages);
    }

    public List<PImage> getImages(Map<String, List<PImage>> images, String key)
    {
        List<PImage> imgs = images.get(key);
        if (imgs == null) {
            imgs = new LinkedList<>();
            images.put(key, imgs);
        }
        return imgs;
    }

    public void nextImage(Entity entity) {
        entity.setImageIndex((entity.getImageIndex() + 1) % entity.getImages().size());
    }
}
