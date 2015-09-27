uniform mat4 u_MVP;
attribute vec4 a_Position;
attribute vec4 a_Color;
varying vec4 v_Color;
varying float v_distance;
varying vec3 v_Position;
uniform int numLightsUsed;
const int numLights = 10;
uniform vec4 l_lights[numLights];
attribute vec4 a_Normal;
varying vec4 v_Normal;

void main() {
   v_Color = a_Color;
   gl_Position = u_MVP * a_Position;
   v_Position = gl_Position.xyz;
   v_Normal = u_MVP * vec4(a_Normal.xyz, 0.0);
   v_distance = 1.0 - ( sqrt(  gl_Position.x * gl_Position.x + gl_Position.y * gl_Position.y + gl_Position.z * gl_Position.z ) / 255.0 );
}
