uniform mat4 u_MVP;
attribute vec4 a_Position;
attribute vec4 a_Color;
attribute vec4 a_Normal;
attribute vec2 aTexCoord;

varying vec4 v_Color;
varying vec3 v_Position;

varying vec2 vTextureCoords;
varying vec4 vNormal;
varying vec4 vTransformedVertex;

void main() {
   v_Color = a_Color;
   vTransformedVertex = u_MVP * a_Position;
   vNormal = u_MVP * a_Normal; //wrong, I know.
   gl_Position = vTransformedVertex;
   vTextureCoords = aTexCoord;
   v_Position = gl_Position.xyz;
}
