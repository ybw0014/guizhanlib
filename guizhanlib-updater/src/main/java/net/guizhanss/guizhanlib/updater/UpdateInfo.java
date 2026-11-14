package net.guizhanss.guizhanlib.updater;

import java.net.URI;

/**
 * Describes a remote update candidate and its integrity metadata.
 * The download URL is the provider's stable endpoint or asset URL, not a pre-resolved redirect
 * target that may expire. A checksum and supported algorithm are required for installation;
 * external candidates are notification-only.
 *
 * @param version the remote version string, compared as supplied by the provider
 * @param downloadUrl the stable download endpoint or asset URL, before redirects
 * @param fileName the safe basename to stage in the server's update folder
 * @param checksum the hexadecimal checksum, required when installing
 * @param checksumAlgorithm the checksum algorithm, required when installing
 * @param external whether the candidate requires a manual download
 */
public record UpdateInfo(String version, URI downloadUrl, String fileName, String checksum,
                         String checksumAlgorithm, boolean external) {}
