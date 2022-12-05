/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.tuberlin.dima.hackit.core.tuple.header;

import de.tuberlin.dima.hackit.core.action.Action;
import de.tuberlin.dima.hackit.core.action.ActionGroup;
import de.tuberlin.dima.hackit.core.tags.HackitTag;
import de.tuberlin.dima.hackit.core.tuple.HackitTuple;
import java.io.Serializable;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;


/**
 * Header is the container of the metadata asociated to one {@link HackitTuple}
 *
 * @param <K> type of the identifier of the {@link HackitTuple}
 */
public abstract class Header<K> implements Serializable, ActionGroup {

    /**
     * id is identifier of the header and also the tuple
     */
    private K id;

    /**
     * child indicate the number of the child that the current header is from the
     * original {@link HackitTuple} and they share the same identifier <code>id</code>
     */
    protected int child = 0;

    /**
     * tags added to the header, this describes some action that need to
     * be apply to the {@link HackitTuple}
     */
    private Set<HackitTag> tags;

    /**
     * during the process of adding news {@link HackitTag} could add a new
     * {@link Action} at the header, and this change the status of
     * <code>has_callback_tag</code>
     */
    private boolean has_callback_tag = false;

    /**
     * during the process of adding news {@link HackitTag} could add a new
     * {@link Action} at the header, and this change the status of
     * <code>has_skip_tag</code>
     */
    private boolean has_skip_tag = false;

    /**
     * during the process of adding news {@link HackitTag} could add a new
     * {@link Action} at the header, and this change the status of
     * <code>has_sendout_tag</code>
     */
    private boolean has_sendout_tag = false;

    /**
     * during the process of adding news {@link HackitTag} could add a new
     * {@link Action} at the header, and this change the status of
     * <code>has_haltjob_tag</code>
     */
    private boolean has_haltjob_tag = false;

    /**
     * Default Construct, this will call {@link #generateID()} and produce the
     * new identifier
     */
    public Header() {
        this.id = generateID();
    }

    /**
     * Construct with the identifier as parameter
     *
     * @param id is the identifier of the Header
     */
    public Header(K id) {
        this.id = id;
    }

    /**
     * Construct with the identifier and child identifier as parameter
     *
     * @param id is the identifier of the Header
     * @param child is the child identifier assigned
     */
    public Header(K id, int child){
        this(id);
        this.child = child;
    }

    /**
     * retrieve the identifier of the Header
     *
     * @return current identifier of type <code>K</code>
     */
    public K getId(){
        return this.id;
    }

    /**
     * Add a {@link HackitTag} that could provide a new {@link Action} to be
     * perfomed by Hackit to the {@link HackitTuple}, also update all the
     * possible action calling the method {@link #updateActionVector(HackitTag)}
     *
     * @param tag {@link HackitTag}
     */
    public void addTag(HackitTag tag){
        if(this.tags == null){
            this.tags = new HashSet<>();
        }
        if(this.tags.contains(tag)){
           return;
        }
        this.tags.add(tag);
        //update all the possible actions on the {@link ActionGroup}
        updateActionVector(tag);
    }

    /**
     * remove all the tags from the header, and set all the possible options as false
     */
    public void clearTags(){
        this.tags.clear();
        this.has_callback_tag = false;
        this.has_haltjob_tag  = false;
        this.has_sendout_tag  = false;
        this.has_skip_tag     = false;
    }

    /**
     * iterate provide an {@link Iterator} that contains all the {@link HackitTag} that were
     * add on the {@link #tags}
     *
     * If the {@link #tags} is null or empty it will return an {@link Collections#emptyIterator()}
     *
     * @return {@link Iterator} with the current {@link HackitTag}'s
     */
    public Iterator<HackitTag> iterate(){
        if(this.tags == null){
            return Collections.emptyIterator();
        }
        if(this.tags.size() == 0){
            return Collections.emptyIterator();
        }
        return this.tags.iterator();
    }

    /**
     * Generate a new header that it related on some way with the father header,
     * depending on the logic of the extender it will be way of generate the child
     *
     *
     * @return new {@link Header} that correspond to the child
     */
    public abstract Header<K> createChild();

    /**
     * Generate a new identifier of type <code>K</code> that it will use inside
     * of {@link #Header()}
     *
     * @return new identifier of type <code>K</code>
     */
    protected abstract K generateID();

    @Override
    public String toString() {
        return "Header{" +
            "id=" + id +
            ", child=" + child +
            ", tags=" + tags +
            ", has_callback_tag=" + has_callback_tag +
            ", has_skip_tag=" + has_skip_tag +
            ", has_sendout_tag=" + has_sendout_tag +
            ", has_haltjob_tag=" + has_haltjob_tag +
            '}';
    }

    /**
     * Update the possible {@link Action} that could be perfomed
     * over the {@link HackitTuple}, this is depends on the new {@link HackitTag}
     *
     * @param tag {@link HackitTag} that could have new {@link Action}'s
     */
    private void updateActionVector(HackitTag tag){
        this.has_callback_tag = tag.hasCallback() || this.has_callback_tag;
        this.has_haltjob_tag  = tag.isHaltJob() || this.has_haltjob_tag;
        this.has_sendout_tag  = tag.isSendOut() || this.has_sendout_tag;
        this.has_skip_tag     = tag.isSkip() || this.has_skip_tag;
    }

    @Override
    public boolean hasCallback() {
        return this.has_callback_tag;
    }

    @Override
    public boolean isHaltJob() {
        return this.has_haltjob_tag;
    }

    @Override
    public boolean isSendOut() {
        return this.has_sendout_tag;
    }

    @Override
    public boolean isSkip() {
        return this.has_skip_tag;
    }
}
