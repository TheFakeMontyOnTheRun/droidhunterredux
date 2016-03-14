precision mediump float;
varying vec4 v_Color;
varying float v_distance;
varying vec3 v_Position;
uniform sampler2D sTexture;
varying vec2 vTextureCoords;


void main() {
    vec4 texel =  texture2D( sTexture, vTextureCoords );
    vec4 finalColor = v_Color + texel;
    gl_FragColor = finalColor;
    gl_FragColor.rgb *= v_distance;
}
