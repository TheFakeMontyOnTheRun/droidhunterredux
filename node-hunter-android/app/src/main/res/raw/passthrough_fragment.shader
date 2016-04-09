precision mediump float;
varying vec2 vTextureCoords;

uniform sampler2D sTexture;
uniform sampler2D sNormalMap;

uniform vec4 uAmbientLightColor;
uniform vec4 uDiffuseLightColor;
uniform vec4 uDiffuseLightDirection;

varying vec4 vNormal;

varying vec4 vTransformedVertex;

varying vec4 v_Color;

//http://chimera.labs.oreilly.com/books/1234000001814/ch08.html#ch08_id35940324
vec3 getNormalFromMapInObjectSpace( const in sampler2D map, const in vec2 uv, const in vec3 normal, const in vec3 tangent ) {
    highp vec3 tangentSpaceNormal = texture2D(map, uv).yzx * 2.0 - 1.0;
    highp vec3 n = normalize(normal);
    highp vec3 t = normalize(tangent);
    highp vec3 crossed = cross(n, t);
    highp vec3 b = normalize(crossed);
    highp mat3 basis = mat3(n, t, b);
    highp vec3 N = basis * tangentSpaceNormal;

    return N;
}



vec4 ComputeLight (const in vec3 direction, const in vec4 lightColor, const in vec3 normal, const in vec3 halfVec, const in vec4 diffuse, const in vec4 specular, const in float shininess) {

        float nDotL = dot(normal, direction)  ;
        vec4 lambert = diffuse * lightColor * max (nDotL, 0.0) ;

        float nDotH = dot(normal, halfVec) ;
        vec4 phong = specular * lightColor * pow (max(nDotH, 0.0), shininess) ;

        return lambert + phong;
}

void main() {

    vec3 direction0 = vec3( -1.0, 0.0, 0.0 );
    vec4 light0posn = uDiffuseLightDirection;
    vec4 light0color = uDiffuseLightColor;

    vec4 texel = texture2D( sTexture, vTextureCoords );

    vec4 interpolatedNormal = vNormal;

    vec3 dehomogenizedPosition = vTransformedVertex.xyz / vTransformedVertex.w;

    vec3 normal = normalize(interpolatedNormal.xyz);

    if ( light0posn.w == 1.0 ) {
        vec3 position0 = light0posn.xyz / light0posn.w;
        direction0 = normalize (position0 - dehomogenizedPosition);
   	} else {
   		direction0 = normalize (vec3(light0posn.x, light0posn.y, light0posn.z ) );
   	}

    const vec3 origin = vec3(0,0,0);
   	vec3 eyedirn = normalize(origin - dehomogenizedPosition);

   	vec3 half0 = normalize (direction0 + eyedirn) ;
   	vec3 N = getNormalFromMapInObjectSpace( sNormalMap, vTextureCoords, normal,  vec3( -1.0, 0.0, 0.0 ) );
    gl_FragColor = ( v_Color + texel ) * ( uAmbientLightColor + ComputeLight(direction0, light0color, N, half0, vec4( 0.5, 0.5, 0.5, 1.0), vec4( 1.0, 1.0, 1.0, 1.0 ), 1.0) );
}
