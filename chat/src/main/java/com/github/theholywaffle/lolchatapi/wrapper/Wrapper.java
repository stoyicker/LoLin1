/*******************************************************************************
 * Copyright (c) 2014 Bert De Geyter (https://github.com/TheHolyWaffle).
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 *
 * Contributors:
 *     Bert De Geyter (https://github.com/TheHolyWaffle)
 ******************************************************************************/
package com.github.theholywaffle.lolchatapi.wrapper;

import com.github.theholywaffle.lolchatapi.LoLChat;

import org.jivesoftware.smack.XMPPConnection;

/**
 * This and all the files in the module have been developed by Bert De Geyter (https://github.com/TheHolyWaffle) and are protected by the Apache GPLv3 license.
 */
public class Wrapper<E> {

    protected XMPPConnection con;
    private E object;
    protected LoLChat api;

    protected Wrapper(LoLChat api, XMPPConnection con, E object) {
        this.con = con;
        this.object = object;
        this.api = api;
    }

    protected E get() {
        return object;
    }

}
