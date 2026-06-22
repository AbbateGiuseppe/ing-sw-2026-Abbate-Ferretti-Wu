package it.polimi.ingsw.gc49.client.gui;

import it.polimi.ingsw.gc49.server.model.Card.Card;
import it.polimi.ingsw.gc49.server.model.Card.BuildingCard.BuildingCard;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

public class ImageAssetManager {
    private static final String GRAPHICS_FOLDER = "graphics";

    private final List<Path> imagePaths = new ArrayList<>();
    private final List<Path> cardImagePool = new ArrayList<>();
    private final java.util.Map<String, ImageIcon> iconCache = new java.util.HashMap<>();
    private final java.util.Map<Path, BufferedImage> imageCache = new java.util.HashMap<>();

    public ImageAssetManager() {
        scanImages();
    }

    public int getCustomerAssetCount() {
        return imagePaths.size();
    }

    public int getCardImageCount() {
        return cardImagePool.size();
    }

    public String getFirstCardImagePath() {
        return cardImagePool.isEmpty() ? "none" : cardImagePool.get(0).toString();
    }

    public Optional<ImageIcon> findIcon(Card card, int width, int height) {
        if (card == null) {
            return Optional.empty();
        }

        Optional<Path> namedGraphicImage = findNamedGraphicImage(card);
        if (namedGraphicImage.isPresent()) {
            return Optional.of(loadIcon(namedGraphicImage.get(), width, height));
        }

        return Optional.empty();
    }

    public Optional<ImageIcon> findDecorativeCardIcon(int seed, int width, int height) {
        return Optional.empty();
    }

    public Optional<Image> findCardImage(Card card, int width, int height) {
        return findIcon(card, width, height).map(ImageIcon::getImage);
    }

    public Optional<BufferedImage> findOfferTrackSheet() {
        return imagePaths.stream()
                .filter(this::looksLikeOfferTrackSheet)
                .min(Comparator.comparing(path -> path.getFileName().toString().toLowerCase(Locale.ROOT)))
                .flatMap(this::loadImage);
    }

    public Optional<BufferedImage> findOrderBoardSheet(int playerCount) {
        String imageName = orderBoardSheetName(playerCount);
        if (imageName == null) {
            return Optional.empty();
        }
        return imagePaths.stream()
                .filter(path -> path.getFileName().toString().equalsIgnoreCase(imageName))
                .findFirst()
                .flatMap(this::loadImage);
    }

    private void scanImages() {
        Path cwd = Paths.get("").toAbsolutePath().normalize();
        Set<Path> uniqueImages = new LinkedHashSet<>();
        List<Path> roots = new ArrayList<>();
        roots.add(cwd);
        if (cwd.getParent() != null) {
            roots.add(cwd.getParent());
        }
        if (cwd.getParent() != null && cwd.getParent().getParent() != null) {
            roots.add(cwd.getParent().getParent());
        }

        for (Path root : roots) {
            if (!Files.isDirectory(root)) {
                continue;
            }
            try (Stream<Path> paths = Files.walk(root, 8)) {
                paths.filter(Files::isRegularFile)
                        .filter(this::isImage)
                        .filter(this::isFromCustomerCardAssetPack)
                        .map(path -> path.toAbsolutePath().normalize())
                        .forEach(uniqueImages::add);
            } catch (Exception ignored) {
                // Assets are optional; text cards remain fully usable.
            }
        }

        imagePaths.addAll(uniqueImages);
        imagePaths.sort(Comparator.comparing(path -> path.toString().toLowerCase(Locale.ROOT)));

        imagePaths.stream()
                .filter(this::looksLikeCardAsset)
                .sorted(Comparator
                        .comparingInt(this::trailingNumber)
                        .thenComparing(path -> path.toString().toLowerCase(Locale.ROOT)))
                .forEach(path -> {
                    cardImagePool.add(path);
                });
    }

    private Optional<Path> findNamedGraphicImage(Card card) {
        List<String> candidates = namedGraphicCandidates(card);
        if (candidates.isEmpty()) {
            return Optional.empty();
        }

        for (String candidate : candidates) {
            String normalizedCandidate = normalize(candidate);
            Optional<Path> match = imagePaths.stream()
                    .filter(path -> path.toString().toLowerCase(Locale.ROOT).contains(GRAPHICS_FOLDER))
                    .filter(path -> normalize(fileNameWithoutExtension(path)).equals(normalizedCandidate))
                    .findFirst();
            if (match.isPresent()) {
                return match;
            }
        }

        for (String candidate : candidates) {
            String normalizedCandidate = normalize(candidate);
            Optional<Path> match = imagePaths.stream()
                    .filter(path -> path.toString().toLowerCase(Locale.ROOT).contains(GRAPHICS_FOLDER))
                    .filter(path -> {
                        String normalizedPath = normalize(path.toString());
                        return normalizedPath.contains(normalizedCandidate)
                                || normalizedCandidate.contains(normalize(fileNameWithoutExtension(path)));
                    })
                    .min(Comparator.comparingInt(path -> path.toString().length()));
            if (match.isPresent()) {
                return match;
            }
        }
        return Optional.empty();
    }

    private List<String> namedGraphicCandidates(Card card) {
        List<String> candidates = new ArrayList<>();
        String type = card.getClass().getSimpleName();

        switch (type) {
            case "Builder" -> appendCandidate(candidates, type, "buildingDiscount", card, "numPoints", card);
            case "Hunter" -> fieldValue(card, "drumstick").ifPresent(value -> candidates.add(type + " " + value));
            case "Shaman" -> fieldValue(card, "numStars").ifPresent(value -> candidates.add(type + " " + value));
            case "Inventor" -> fieldValue(card, "invention").ifPresent(value -> candidates.add(type + " " + value));
            case "HuntingEvent" -> fieldValue(card, "pointsPerHunter").ifPresent(value -> candidates.add(type + " " + value));
            case "SustenanceEvent" -> fieldValue(card, "minusPoints").ifPresent(value -> candidates.add(type + " " + value));
            case "RitualEvent" -> fieldValue(card, "plusPoints").ifPresent(plus ->
                    fieldValue(card, "minusPoints").ifPresent(minus -> candidates.add(type + " " + plus + " " + minus)));
            case "PaintingEvent" -> fieldValue(card, "threshold").ifPresent(threshold ->
                    fieldValue(card, "plusPoints").ifPresent(plus ->
                            fieldValue(card, "minusPoints").ifPresent(minus ->
                                    candidates.add(type + " " + (((Number) threshold).intValue() + 1)
                                            + " " + plus + " " + minus))));
            case "TwentyFiveBonusPointsEndGame" -> fieldValue(card, "foodPrice")
                    .ifPresent(foodPrice -> candidates.add("0 " + foodPrice + " " + type));
            default -> {
                if (card instanceof BuildingCard) {
                    Optional<Object> pointsEndgame = fieldValue(card, "pointsEndgame");
                    Optional<Object> foodPrice = fieldValue(card, "foodPrice");
                    if (pointsEndgame.isPresent() && foodPrice.isPresent()) {
                        StringBuilder builder = new StringBuilder()
                                .append(pointsEndgame.get())
                                .append(" ")
                                .append(foodPrice.get())
                                .append(" ")
                                .append(type);
                        fieldValue(card, "unit").ifPresent(value -> builder.append(" ").append(value));
                        fieldValue(card, "pointsPerUnit").ifPresent(value -> builder.append(" ").append(value));
                        candidates.add(builder.toString());
                    }
                } else {
                    candidates.add(type);
                }
            }
        }

        return candidates;
    }

    private void appendCandidate(List<String> candidates, String type, String firstField, Card card, String secondField, Card sameCard) {
        fieldValue(card, firstField).ifPresent(first ->
                fieldValue(sameCard, secondField).ifPresent(second ->
                        candidates.add(type + " " + first + " " + second)));
    }

    private Optional<Object> fieldValue(Object target, String fieldName) {
        Class<?> current = target.getClass();
        while (current != null) {
            try {
                Field field = current.getDeclaredField(fieldName);
                field.setAccessible(true);
                return Optional.ofNullable(field.get(target));
            } catch (NoSuchFieldException e) {
                current = current.getSuperclass();
            } catch (Exception e) {
                return Optional.empty();
            }
        }
        return Optional.empty();
    }

    private boolean isImage(Path path) {
        String name = path.getFileName().toString().toLowerCase(Locale.ROOT);
        return name.endsWith(".jpg") || name.endsWith(".jpeg") || name.endsWith(".png");
    }

    private boolean isFromCustomerCardAssetPack(Path path) {
        String fullPath = path.toString().toLowerCase(Locale.ROOT);
        return fullPath.contains(GRAPHICS_FOLDER);
    }

    private ImageIcon loadIcon(Path path, int width, int height) {
        String key = path.toAbsolutePath() + ":" + width + "x" + height;
        return iconCache.computeIfAbsent(key, unused -> {
            try {
                BufferedImage source = ImageIO.read(path.toFile());
                if (source == null) {
                    return new ImageIcon();
                }
                Dimension size = fitSize(source.getWidth(), source.getHeight(), width, height);
                BufferedImage scaled = new BufferedImage(size.width, size.height, BufferedImage.TYPE_INT_ARGB);
                Graphics2D graphics = scaled.createGraphics();
                graphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
                graphics.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                graphics.drawImage(source, 0, 0, size.width, size.height, null);
                graphics.dispose();
                return new ImageIcon(scaled);
            } catch (Exception ignored) {
                return new ImageIcon();
            }
        });
    }

    private boolean looksLikeCardAsset(Path path) {
        String name = path.getFileName().toString().toLowerCase(Locale.ROOT);
        String fullPath = path.toString().toLowerCase(Locale.ROOT);
        if (!fullPath.contains(GRAPHICS_FOLDER) || name.contains("pedine")) {
            return false;
        }
        try {
            BufferedImage image = ImageIO.read(path.toFile());
            if (image == null) {
                return false;
            }
            double ratio = (double) image.getWidth() / (double) image.getHeight();
            return image.getWidth() >= 350
                    && image.getHeight() >= 500
                    && ratio > 0.55
                    && ratio < 0.85;
        } catch (Exception ignored) {
            return false;
        }
    }

    private boolean looksLikeOfferTrackSheet(Path path) {
        String name = path.getFileName().toString().toLowerCase(Locale.ROOT);
        if (!name.startsWith("4adc") || !name.endsWith("-0.jpg")) {
            return false;
        }
        try {
            long size = Files.size(path);
            BufferedImage image = ImageIO.read(path.toFile());
            return size > 2_000_000
                    && image != null
                    && image.getWidth() >= 1500
                    && image.getHeight() >= 1500;
        } catch (Exception ignored) {
            return false;
        }
    }

    private Optional<BufferedImage> loadImage(Path path) {
        try {
            Path absolutePath = path.toAbsolutePath().normalize();
            BufferedImage image = imageCache.get(absolutePath);
            if (image == null) {
                image = ImageIO.read(absolutePath.toFile());
                if (image != null) {
                    imageCache.put(absolutePath, image);
                }
            }
            return Optional.ofNullable(image);
        } catch (Exception ignored) {
            return Optional.empty();
        }
    }

    private String orderBoardSheetName(int playerCount) {
        switch (playerCount) {
            case 2:
                return "ad2302b8c19f4f5ea61d61c1a7b26b46HmZxgem8S8StPBcm-1.jpg";
            case 3:
                return "4adc68c79d064367fe9baa6cebf46e81esRnQemeKziETipC-1.jpg";
            case 4:
                return "ad2302b8c19f4f5ea61d61c1a7b26b46HmZxgem8S8StPBcm-0.jpg";
            case 5:
                return "4adc68c79d064367fe9baa6cebf46e81esRnQemeKziETipC-0.jpg";
            default:
                return null;
        }
    }

    private int trailingNumber(Path path) {
        String name = fileNameWithoutExtension(path);
        int dash = name.lastIndexOf('-');
        if (dash < 0 || dash == name.length() - 1) {
            return Integer.MAX_VALUE;
        }
        try {
            return Integer.parseInt(name.substring(dash + 1));
        } catch (NumberFormatException e) {
            return Integer.MAX_VALUE;
        }
    }

    private Dimension fitSize(int originalWidth, int originalHeight, int maxWidth, int maxHeight) {
        if (originalWidth <= 0 || originalHeight <= 0) {
            return new Dimension(maxWidth, maxHeight);
        }
        double ratio = Math.min((double) maxWidth / originalWidth, (double) maxHeight / originalHeight);
        return new Dimension(Math.max(1, (int) Math.round(originalWidth * ratio)),
                Math.max(1, (int) Math.round(originalHeight * ratio)));
    }

    private String safeSimpleToString(Card card) {
        try {
            return card.simpleToString();
        } catch (Exception e) {
            return "";
        }
    }

    private String fileNameWithoutExtension(Path path) {
        String name = path.getFileName().toString();
        int dot = name.lastIndexOf('.');
        return dot > 0 ? name.substring(0, dot) : name;
    }

    private String normalize(String text) {
        if (text == null) {
            return "";
        }
        return text.toLowerCase(Locale.ROOT).replaceAll("[^a-z0-9]+", "");
    }
}
