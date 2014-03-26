# script to generate keystore because android doesn't like itch.io's ssl cert
# based on http://blog.crazybob.org/2010/02/android-trusting-ssl-certificates.html

export CLASSPATH=~/downloads/bcprov-jdk15on-146.jar
CERTSTORE=res/raw/itchstore.bks
if [ -a $CERTSTORE ]; then
    rm $CERTSTORE || exit 1
fi
keytool \
      -import \
      -v \
      -trustcacerts \
      -alias 0 \
      -file <(openssl x509 -in itch.pem) \
      -keystore $CERTSTORE \
      -storetype BKS \
      -provider org.bouncycastle.jce.provider.BouncyCastleProvider \
      -providerpath /usr/share/java/bcprov.jar \
      -storepass ez24get
