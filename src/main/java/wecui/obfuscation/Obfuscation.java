package wecui.obfuscation;

import deobf.Entity;
import deobf.EntityClientPlayerMP;
import deobf.EntityPlayerSP;
import deobf.GuiChat;
import deobf.GuiScreen;
import deobf.NetClientHandler;
import deobf.Packet3Chat;
import deobf.RenderHelper;
import deobf.Tessellator;
import deobf.World;
import java.io.File;
import java.lang.reflect.Method;
import net.minecraft.client.Minecraft;
import wecui.InitializationFactory;
import wecui.WorldEditCUI;
import wecui.render.RenderEntity;

/**
 * Main obfuscation class
 * Combines all obfuscated classes and methods into a single class
 * Eases updates, cleans up the rest of the codebase.
 * 
 * TODO: Clean this up
 * 
 * @author lahwran
 * @author yetanotherx
 * 
 * @obfuscated
 */
public class Obfuscation implements InitializationFactory {

    protected WorldEditCUI controller;
    protected Minecraft minecraft;
    protected Tessellator tessellator = Tessellator.a;

    public Obfuscation(WorldEditCUI controller) {
        this.controller = controller;
    }

    @Override
    public void initialize() {
        this.minecraft = this.controller.getMinecraft();
    }

    public static boolean isMultiplayerWorld(Minecraft mc) {
        return mc.l();
    }

    public boolean isMultiplayerWorld() {
        return isMultiplayerWorld(minecraft);
    }

    public void sendChat(String chat) {
        minecraft.h.a(chat);
    }

    /**
     * Displays a chat message on the screen, if the player is currently playing
     * @param chat 
     */
    public void showChatMessage(String chat) {
        if (minecraft.h != null) {
            minecraft.h.b(chat);
        }
    }

    public void showGuiScreenIfGuiChat(GuiScreen screen) {
        GuiScreen currentScreen = minecraft.s;
        if (currentScreen == null || currentScreen.getClass() == GuiChat.class) {
            try {
                Method setScreen = minecraft.getClass().getDeclaredMethod("a", GuiScreen.class);
                setScreen.invoke(minecraft, (GuiScreen) null);
                setScreen.invoke(minecraft, screen);
            } catch (Exception ex) {
            }
        }
    }

    public void draw_begin(int type) {
        tessellator.a(type);
    }

    public double getPlayerX(float renderTick) {
        EntityPlayerSP plyr = minecraft.h;
        return plyr.p + ((plyr.s - plyr.p) * renderTick);
    }

    public double getPlayerY(float renderTick) {
        EntityPlayerSP plyr = minecraft.h;
        return plyr.q + ((plyr.t - plyr.q) * renderTick);
    }

    public double getPlayerZ(float renderTick) {
        EntityPlayerSP plyr = minecraft.h;
        return plyr.r + ((plyr.u - plyr.r) * renderTick);
    }

    public static double getPlayerX(EntityPlayerSP player) {
        return player.s;
    }

    public static double getPlayerY(EntityPlayerSP player) {
        return player.t;
    }

    public static double getPlayerZ(EntityPlayerSP player) {
        return player.u;
    }

    public void addVertex(double x, double y, double z) {
        tessellator.a(x, y, z);
    }

    public void draw() {
        tessellator.a();
    }

    public static EntityPlayerSP getPlayer(Minecraft mc) {
        return mc.h;
    }

    public EntityPlayerSP getPlayer() {
        return getPlayer(minecraft);
    }

    public static World getWorld(Minecraft mc) {
        return mc.f;
    }

    public World getWorld() {
        return getWorld(minecraft);
    }

    public void spawnEntity(Entity entity) {
        Minecraft mc = this.controller.getMinecraft();

        entity = new RenderEntity(this.controller, getWorld(mc));
        doSomethingWithEntityCoordinates(mc, entity);
        getWorld(mc).a(entity);
        doSomethingWithEntityCoordinates(mc, entity);
        controller.getDebugger().debug("RenderEntity spawned");

    }

    public static void doSomethingWithEntityCoordinates(Minecraft mc, Entity entity) {
        entity.d(getPlayerX(mc.h), getPlayerY(mc.h), getPlayerZ(mc.h));
    }

    public static void disableLighting() {
        RenderHelper.a();
    }

    public static void enableLighting() {
        RenderHelper.b();
    }

    public NetClientHandler getNetClientHandler(EntityClientPlayerMP player) {
        return player.a;
    }

    public static String getChatMessage(Packet3Chat packet) {
        return packet.a;
    }

    public static void putToMCHash(MCHash hash, Object first, Object second) {
        hash.a(first, second);
    }

    public static File getAppDir(String app) {
        return Minecraft.a(app);
    }

    public static File getMinecraftDir() {
        return Minecraft.b();
    }

    public static File getModDir() {
        return new File(getMinecraftDir(), "mods" + File.separator + "WorldEditCUI");
    }
}
