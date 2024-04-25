/*
 * Copyright (C) 2015, 2024, Matthias Sohn <matthias.sohn@sap.com> and others
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Distribution License v. 1.0 which is available at
 * https://www.eclipse.org/org/documents/edl-v10.php.
 *
 * SPDX-License-Identifier: BSD-3-Clause
 */
package org.eclipse.jgit.lfs.server.fs;

import static org.eclipse.jgit.util.HttpSupport.HDR_AUTHORIZATION;

import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Collections;

import org.eclipse.jgit.annotations.Nullable;
import org.eclipse.jgit.lfs.internal.AtomicObjectOutputStream;
import org.eclipse.jgit.lfs.lib.AnyLongObjectId;
import org.eclipse.jgit.lfs.lib.Constants;
import org.eclipse.jgit.lfs.server.LargeFileRepository;
import org.eclipse.jgit.lfs.server.Response;
import org.eclipse.jgit.lfs.server.Response.Action;

/**
 * Repository storing large objects in the file system
 *
 * @since 4.3
 */
public class FileLfsRepository implements LargeFileRepository {

	private String url;
	private final Path dir;

	/**
	 * <p>
	 * Constructor for FileLfsRepository.
	 * </p>
	 *
	 * @param url
	 *            external URL of this repository
	 * @param dir
	 *            storage directory
	 * @throws java.io.IOException
	 *             if an IO error occurred
	 */
	public FileLfsRepository(String url, Path dir) throws IOException {
		this.url = url;
		this.dir = dir;
		Files.createDirectories(dir);
	}

	@Override
	public Response.Action getDownloadAction(AnyLongObjectId id) {
		return getAction(id);
	}

	@Override
	public Action getUploadAction(AnyLongObjectId id, long size) {
		return getAction(id);
	}

	@Override
	@Nullable
	public Action getVerifyAction(AnyLongObjectId id) {
		return getAction(id);
	}

	@Override
	public long getSize(AnyLongObjectId id) throws IOException {
		Path p = getPath(id);
		if (Files.exists(p)) {
			return Files.size(p);
		}
		return -1;
	}

	/**
	 * Get the storage directory
	 *
	 * @return the path of the storage directory
	 */
	public Path getDir() {
		return dir;
	}

	/**
	 * Get the path where the given object is stored
	 *
	 * @param id
	 *            id of a large object
	 * @return path the object's storage path
	 */
	protected Path getPath(AnyLongObjectId id) {
		StringBuilder s = new StringBuilder(
				Constants.LONG_OBJECT_ID_STRING_LENGTH + 6);
		s.append(toHexCharArray(id.getFirstByte())).append('/');
		s.append(toHexCharArray(id.getSecondByte())).append('/');
		s.append(id.name());
		return dir.resolve(s.toString());
	}

	private Response.Action getAction(AnyLongObjectId id) {
		Response.Action a = new Response.Action();
		a.href = url + id.getName();
		a.header = Collections.singletonMap(HDR_AUTHORIZATION, "not:required"); //$NON-NLS-1$
		return a;
	}

	ReadableByteChannel getReadChannel(AnyLongObjectId id)
			throws IOException {
		return FileChannel.open(getPath(id), StandardOpenOption.READ);
	}

	AtomicObjectOutputStream getOutputStream(AnyLongObjectId id)
			throws IOException {
		Path path = getPath(id);
		Path parent = path.getParent();
		if (parent != null) {
			Files.createDirectories(parent);
		}
		return new AtomicObjectOutputStream(path, id);
	}

	private static char[] toHexCharArray(int b) {
		final char[] dst = new char[2];
		formatHexChar(dst, 0, b);
		return dst;
	}

	private static final char[] hexchar = { '0', '1', '2', '3', '4', '5', '6',
			'7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

	private static void formatHexChar(char[] dst, int p, int b) {
		int o = p + 1;
		while (o >= p && b != 0) {
			dst[o--] = hexchar[b & 0xf];
			b >>>= 4;
		}
		while (o >= p)
			dst[o--] = '0';
	}

	/**
	 * Get URL of content server
	 *
	 * @return the url of the content server
	 * @since 4.11
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * Set the URL of the content server
	 *
	 * @param url
	 *            the url of the content server
	 * @since 4.11
	 */
	public void setUrl(String url) {
		this.url = url;
	}
}
