/*
 * This file is part of GumTree.
 *
 * GumTree is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * GumTree is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with GumTree.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Copyright 2011-2015 Jean-Rémy Falleri <jr.falleri@gmail.com>
 * Copyright 2011-2015 Floréal Morandat <florealm@gmail.com>
 */

package com.github.gumtreediff.client.diff.web;

import com.github.gumtreediff.actions.EditScript;
import com.github.gumtreediff.actions.EditScriptGenerator;
import com.github.gumtreediff.io.ActionsIoUtils;
import com.github.gumtreediff.matchers.MappingStore;
import com.github.gumtreediff.matchers.Matcher;
import com.github.gumtreediff.tree.TreeContext;
import org.rendersnake.DocType;
import org.rendersnake.HtmlCanvas;
import org.rendersnake.Renderable;

import java.io.File;
import java.io.IOException;

import static org.rendersnake.HtmlAttributesFactory.*;

public class TextDiffView implements Renderable {

    private final MappingStore mappings;

    private TreeContext src;

    private TreeContext dst;

    private File fSrc;

    private File fDst;

    private EditScript script;

    public TextDiffView(File fSrc, File fDst, TreeContext src, TreeContext dst, Matcher matcher, EditScriptGenerator scriptGenerator) throws IOException {
        this.fSrc = fSrc;
        this.fDst = fDst;
        this.src = src;
        this.dst = dst;
        this.mappings = matcher.match(src.getRoot(), dst.getRoot());
        this.script = scriptGenerator.computeActions(mappings);
    }

    @Override
    public void renderOn(HtmlCanvas html) throws IOException {
        html
            .render(DocType.HTML5)
            .html(lang("en"))
                .render(new Header())
                .body()
                    .div(class_("container"))
                        .div(class_("row"))
                            .render(new MenuBar())
                        ._div()
                        .div(class_("row"))
                            .div(class_("col-lg-12"))
                                .h3()
                                    .write("Raw edit script ")
                                    .small().content(String.format("%s -> %s", fSrc.getName(), fDst.getName()))
                                ._h3()
                                .pre(class_("border p-2")).content(ActionsIoUtils.toText(src, script, mappings).toString())
                            ._div()
                        ._div()
                    ._div()
                ._body()
            ._html();
    }

    private static class Header implements Renderable {
        @Override
        public void renderOn(HtmlCanvas html) throws IOException {
             html
                     .head()
                        .meta(charset("utf8"))
                        .meta(name("viewport").content("width=device-width, initial-scale=1.0"))
                        .title().content("GumTree")
                        .macros().stylesheet("https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/css/bootstrap.min.css")
                        .macros().javascript("https://code.jquery.com/jquery-3.4.1.min.js")
                        .macros().javascript("https://cdn.jsdelivr.net/npm/popper.js@1.16.0/dist/umd/popper.min.js")
                        .macros().javascript("https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/js/bootstrap.min.js")
                        .macros().javascript("/dist/shortcuts.js")
                     ._head();
        }
    }

    private static class MenuBar implements Renderable {
        @Override
        public void renderOn(HtmlCanvas html) throws IOException {
            html
                    .div(class_("col"))
                        .div(class_("btn-toolbar justify-content-end"))
                            .div(class_("btn-group"))
                                .a(class_("btn btn-default btn-sm btn-danger").href("/quit")).content("Quit")
                            ._div()
                        ._div()
                    ._div();
        }
    }
}