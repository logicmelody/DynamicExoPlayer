package com.auth0.android.jwt;

import java.util.Date;
import java.util.List;
import java.util.Map;

class JWTPayload {

    final String iss;
    final String sub;
    final Date exp;
    final Date nbf;
    final Date iat;
    final String jti;
    final List<String> aud;
    private final  Map<String, Claim> tree;
	final List<String> scopes;

	JWTPayload(String iss, String sub, Date exp, Date nbf, Date iat, String jti,
	                     List<String> aud, Map<String, Claim> tree, List<String> scopes) {
		this.iss = iss;
		this.sub = sub;
		this.exp = exp;
		this.nbf = nbf;
		this.iat = iat;
		this.jti = jti;
		this.aud = aud;
		this.tree = tree;
		this.scopes = scopes;
	}
    JWTPayload(String iss, String sub, Date exp, Date nbf, Date iat, String jti,
               List<String> aud, Map<String, Claim> tree) {
        this(iss, sub, exp, nbf, iat, jti, aud, tree, null);
    }

    Claim claimForName(String name) {
        final Claim claim = this.tree.get(name);
        return claim != null ? claim : new BaseClaim();
    }
}
