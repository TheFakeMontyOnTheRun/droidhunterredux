precision mediump float;
varying vec4 v_Color;
varying float v_distance;
const int numLights = 10;
uniform vec4 l_lights[numLights];
uniform int numLightsUsed;
varying vec4 v_Normal;
varying vec3 v_Position;

vec4 ComputeLight (vec3 direction, vec4 lightcolor, vec3 normal, vec3 halfvec, vec4 mydiffuse, vec4 myspecular, float myshininess) {

        float nDotL = dot(normal, direction)  ;
        vec4 lambert = mydiffuse * lightcolor * max (nDotL, 0.0) ;

        float nDotH = dot(normal, halfvec) ;
        vec4 phong = myspecular * lightcolor * pow (max(nDotH, 0.0), myshininess) ;

        vec4 retval = lambert + phong ;
        return retval ;
}

void main() {

    vec4 light0posn;
    vec4 light0color;
    vec3 position0;
    vec3 direction0;
    vec3 eyepos = vec3( 0, 0, 0 );
    vec3 eyedirn = normalize(eyepos - v_Position);
    vec3 half0;
    vec4 finalColor = v_Color;

    light0color = vec4( 1, 1, 1, 1 );

        for ( int c = 0; c < numLightsUsed; ++c ) {
        	light0posn = l_lights[ c ];
			position0 = light0posn.xyz / light0posn.w;
        	direction0 = normalize (position0 - v_Position);
            half0 = normalize (direction0 + eyedirn) ;
            finalColor = ComputeLight(direction0, vec4( 1, 1, 1, 1 ), v_Normal.xyz, half0, vec4( 1, 1, 1, 1 ), vec4( 1, 1, 1, 1 ), 100.0 );
   		}

    gl_FragColor = finalColor;
    gl_FragColor.rgb *= v_distance;
}
