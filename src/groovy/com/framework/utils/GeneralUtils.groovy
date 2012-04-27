package com.framework.utils

import groovyx.gpars.GParsPool

import java.util.zip.GZIPInputStream
import java.util.zip.GZIPOutputStream


class GeneralUtils {
	/**
	 * Compress the given array of bytes.
	 */
	public static byte[] compress(byte[] inData) {
		if (inData == null) {
			throw new NullPointerException("Can't compress null");
		}

		if (inData.size() == 0) return inData

		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		GZIPOutputStream gz = null;
		try {
			gz = new GZIPOutputStream(bos);
			gz.write(inData);
		} catch (IOException e) {
			throw new RuntimeException("IO exception compressing data", e);
		} finally {
			if (gz != null) {
				try {
					gz.close();
				} catch (IOException e) {
					//                    log.error("Close GZIPOutputStream error", e);
				}
			}
			if (bos != null) {
				try {
					bos.close();
				} catch (IOException e) {
					//                    log.error("Close ByteArrayOutputStream error", e);
				}
			}
		}
		byte[] rv = bos.toByteArray();
		//        log.debug {"Compressed $inData.length bytes to $rv.length"}

		return rv;
	}

	/**
	 * Decompress the given array of bytes.
	 *
	 * @return null if the bytes cannot be decompressed
	 */
	public static byte[] decompress(byte[] inData) {
		assert inData != null, "Can't decompress null"

		if (inData.size() == 0) return inData

		ByteArrayOutputStream bos = null;
		if (inData != null) {
			ByteArrayInputStream bis = new ByteArrayInputStream(inData);
			bos = new ByteArrayOutputStream();
			GZIPInputStream gis;
			try {
				gis = new GZIPInputStream(bis);

				byte[] buf = new byte[8192];
				int r = -1;
				while ((r = gis.read(buf)) > 0) {
					bos.write(buf, 0, r);
				}
			} catch (IOException e) {
				//                log.error("Failed to decompress data", e);
				bos = null;
			}
		}
		return bos == null ? null : bos.toByteArray();
	}

	/**
	 * Call a closure async with a timeout option when timeoutInMillis > 0.
	 */
	static def callWithTimeout(int timeoutInMillis, Closure closure) {
		if (timeoutInMillis) {
			GParsPool.withPool(1) {
				return closure.callTimeoutAsync(timeoutInMillis).get()
			}
		} else {
			return closure.call()
		}
	}
}
