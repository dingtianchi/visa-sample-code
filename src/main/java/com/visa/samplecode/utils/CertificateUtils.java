/*
 * *© Copyright 2021 Visa. All Rights Reserved.**
 *
 * NOTICE: The software and accompanying information and documentation (together, the “Software”) remain the property of
 * and are proprietary to Visa and its suppliers and affiliates. The Software remains protected by intellectual property
 * rights and may be covered by U.S. and foreign patents or patent applications. The Software is licensed and not sold.*
 *
 * By accessing the Software you are agreeing to Visa's terms of use (developer.visa.com/terms) and privacy policy (developer.visa.com/privacy).
 * In addition, all permissible uses of the Software must be in support of Visa products, programs and services provided
 * through the Visa Developer Program (VDP) platform only (developer.visa.com). **THE SOFTWARE AND ANY ASSOCIATED
 * INFORMATION OR DOCUMENTATION IS PROVIDED ON AN “AS IS,” “AS AVAILABLE,” “WITH ALL FAULTS” BASIS WITHOUT WARRANTY OR
 * CONDITION OF ANY KIND. YOUR USE IS AT YOUR OWN RISK.** All brand names are the property of their respective owners, used for identification purposes only, and do not imply
 * product endorsement or affiliation with Visa. Any links to third party sites are for your information only and equally
 * do not constitute a Visa endorsement. Visa has no insight into and control over third party content and code and disclaims
 * all liability for any such components, including continued availability and functionality. Benefits depend on implementation
 * details and business factors and coding steps shown are exemplary only and do not reflect all necessary elements for the
 * described capabilities. Capabilities and features are subject to Visa’s terms and conditions and may require development,
 * implementation and resources by you based on your business and operational details. Please refer to the specific
 * API documentation for details on the requirements, eligibility and geographic availability.*
 *
 * This Software includes programs, concepts and details under continuing development by Visa. Any Visa features,
 * functionality, implementation, branding, and schedules may be amended, updated or canceled at Visa’s discretion.
 * The timing of widespread availability of programs and functionality is also subject to a number of factors outside Visa’s control,
 * including but not limited to deployment of necessary infrastructure by issuers, acquirers, merchants and mobile device manufacturers.
 *
 *
 *  This sample code is licensed only for use in a non-production environment for sandbox testing. See the license for all terms of use.
 */

package com.visa.samplecode.utils;

import javax.xml.bind.DatatypeConverter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.security.KeyFactory;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Scanner;

public final class CertificateUtils {

    private static final String LINE_SEPARATOR = "\r\n";
    private static final String BEGIN_CERTIFICATE = "-----BEGIN CERTIFICATE-----";
    private static final String END_CERTIFICATE = "-----END CERTIFICATE-----";
    private static final String BEGIN_PUBLIC_KEY = "-----BEGIN PUBLIC KEY-----";
    private static final String END_PUBLIC_KEY = "-----END PUBLIC KEY-----";
    private static final String BEGIN_PRIVATE_KEY = "-----BEGIN PRIVATE KEY-----";
    private static final String END_PRIVATE_KEY = "-----END PRIVATE KEY-----";

    private CertificateUtils() {
    }

    /**
     * Load Public Key From File
     *
     * @param publicKeyFile - Public key file path
     * @return
     * @throws IOException
     * @throws GeneralSecurityException
     */
    public static RSAPublicKey loadPublicKeyFromFile(String publicKeyFile) throws IOException, GeneralSecurityException {
        String publicKeyPem = getFileContent(publicKeyFile);
        return loadPublicKeyFromPem(publicKeyPem);
    }

    /**
     * Load Public Key from PEM Data
     *
     * @param publicKeyPem - Public Key PEM Data
     * @return
     * @throws GeneralSecurityException
     */
    public static RSAPublicKey loadPublicKeyFromPem(String publicKeyPem) throws GeneralSecurityException {
        String publicKeyPEMStr = publicKeyPem;
        if (publicKeyPem.startsWith(BEGIN_CERTIFICATE)) {
            publicKeyPEMStr = publicKeyPEMStr.replace(BEGIN_CERTIFICATE, "")
                    .replaceAll(LINE_SEPARATOR, "")
                    .replace(END_CERTIFICATE, "");
            return (RSAPublicKey) getX509Certificate(publicKeyPEMStr.trim()).getPublicKey();
        } else if (publicKeyPEMStr.startsWith(BEGIN_PUBLIC_KEY)) {
            publicKeyPEMStr = publicKeyPEMStr.replace(BEGIN_PUBLIC_KEY, "")
                    .replaceAll(LINE_SEPARATOR, "")
                    .replace(END_PUBLIC_KEY, "");
            byte[] encoded = DatatypeConverter.parseBase64Binary(publicKeyPEMStr.trim());
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(encoded);
            return (RSAPublicKey) keyFactory.generatePublic(keySpec);
        }
        throw new IllegalArgumentException("Content is not a certificate or public key in PEM format");
    }

    /**
     * Load Certificate Pem into {@link X509Certificate}
     *
     * @param certificateString
     * @return
     * @throws CertificateException
     */
    private static X509Certificate getX509Certificate(String certificateString) throws CertificateException {
        CertificateFactory cfb = CertificateFactory.getInstance("X509");
        InputStream inputStream = new ByteArrayInputStream(DatatypeConverter.parseBase64Binary(certificateString));
        return (X509Certificate) cfb.generateCertificate(inputStream);
    }

    /**
     * Load Private Key From File
     *
     * @param privateKeyPath - Private key file path
     * @return
     * @throws IOException
     * @throws GeneralSecurityException
     */
    public static RSAPrivateKey loadPrivateKeyFromFile(String privateKeyPath) throws IOException, GeneralSecurityException {
        String privateKeyPem = getFileContent(privateKeyPath);
        return loadPrivateKeyFromPem(privateKeyPem);
    }

    /**
     * Load Private Key from PEM Data
     *
     * @param privateKeyPem - Private Key PEM Data
     * @return
     * @throws GeneralSecurityException
     */
    public static RSAPrivateKey loadPrivateKeyFromPem(String privateKeyPem) throws GeneralSecurityException {
        String privateKey = privateKeyPem.replace(BEGIN_PRIVATE_KEY, "")
                .replaceAll(LINE_SEPARATOR, "")
                .replace(END_PRIVATE_KEY, "");
        byte[] encodedPrivateKey = DatatypeConverter.parseBase64Binary(privateKey);
        return (RSAPrivateKey) KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(encodedPrivateKey));
    }

    /**
     * Get File Content
     *
     * @param filePath - File Path
     * @return
     */
    private static String getFileContent(String filePath) throws IOException {
        final StringBuilder sb = new StringBuilder();
        File file = new File(filePath);
        if (file.exists()) {
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                sb.append(scanner.nextLine()).append(LINE_SEPARATOR);
            }
            scanner.close();
            return sb.toString();
        } else {
            throw new IllegalArgumentException("File does not exist");
        }
    }
}
