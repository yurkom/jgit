/*
 * Copyright (C) 2015, 2024, Matthias Sohn <matthias.sohn@sap.com> and others
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Distribution License v. 1.0 which is available at
 * https://www.eclipse.org/org/documents/edl-v10.php.
 *
 * SPDX-License-Identifier: BSD-3-Clause
 */
package org.eclipse.jgit.lfs.internal;

import org.eclipse.jgit.nls.NLS;
import org.eclipse.jgit.nls.TranslationBundle;

/**
 * Translation bundle for JGit LFS server
 */
@SuppressWarnings("MissingSummary")
public class LfsText extends TranslationBundle {

	/**
	 * Get an instance of this translation bundle.
	 *
	 * @return an instance of this translation bundle
	 */
	public static LfsText get() {
		return NLS.getBundleFor(LfsText.class);
	}

	// @formatter:off
	/***/ public String corruptLongObject;
	/***/ public String dotLfsConfigReadFailed;
	/***/ public String inconsistentContentLength;
	/***/ public String inconsistentMediafileLength;
	/***/ public String incorrectLONG_OBJECT_ID_LENGTH;
	/***/ public String invalidLongId;
	/***/ public String invalidLongIdLength;
	/***/ public String lfsFailedToGetRepository;
	/***/ public String lfsNoDownloadUrl;
	/***/ public String lfsUnauthorized;
	/***/ public String lfsUnavailable;
	/***/ public String missingLocalObject;
	/***/ public String protocolError;
	/***/ public String repositoryNotFound;
	/***/ public String repositoryReadOnly;
	/***/ public String requiredHashFunctionNotAvailable;
	/***/ public String serverFailure;
	/***/ public String verifyFailure;
	/***/ public String wrongAmountOfDataReceived;
}
