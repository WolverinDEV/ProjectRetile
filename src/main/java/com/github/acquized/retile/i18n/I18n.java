/*
 * Copyright 2016 Acquized
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.acquized.retile.i18n;

import com.github.acquized.retile.ProjectRetile;
import com.github.acquized.retile.utils.Utility;

import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.util.Locale;
import java.util.ResourceBundle;

public class I18n {

    public static final String[] SUPPORTED_LOCALES = { "en", "fr", "es", "de" };
    public static final File DIRECTORY = new File(ProjectRetile.getInstance().getDataFolder() + File.separator + "locale");

    public static ResourceBundle bundle;

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public I18n() {
        try {
            if (!DIRECTORY.isDirectory()) {
                DIRECTORY.mkdirs();
            }
            for (String l : SUPPORTED_LOCALES) {
                File f = new File(DIRECTORY, "messages_" + l + ".properties");
                if (!f.exists()) {
                    Files.copy(ProjectRetile.getInstance().getResourceAsStream("messages/" + f.getName()), f.toPath());
                }
            }
        } catch (IOException ex) {
            ProjectRetile.getInstance().getLog().error("Could not create Messages files.", ex);
        }
    }

    public static String getMessage(String key) {
        return Utility.format(ProjectRetile.getInstance().getConfig().getString("General.prefix") + bundle.getString(key).replace("\\n", "\n"));
    }

    public static String getMessage(String key, Object... obj) {
        return Utility.format(ProjectRetile.getInstance().getConfig().getString("General.prefix") + bundle.getString(key).replace("\\n", "\n"), obj);
    }


    // Shortcuts for Static Imports

    public static BaseComponent[] tl(String key) {
        return TextComponent.fromLegacyText(getMessage(key));
    }

    public static BaseComponent[] tl(String key, Object... obj) {
        return TextComponent.fromLegacyText(getMessage(key, obj));
    }

    public void load() {
        try {
            ClassLoader loader = new URLClassLoader(new URL[]{DIRECTORY.toURI().toURL()});
            bundle = ResourceBundle.getBundle("messages", new Locale(ProjectRetile.getInstance().getConfig().getString("General.locale")), loader);
        } catch (IOException ex) {
            ProjectRetile.getInstance().getLog().error("Could not load messages_" + ProjectRetile.getInstance().getConfig().getString("General.locale") + ".properties file. " +
                    "Please check for errors.", ex);
        }
    }

}
