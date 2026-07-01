<template>
  <v-app :theme="'barathym'" style="height: 100dvh; overflow: hidden;">
    <router-view />
  </v-app>
</template>

<script setup lang="ts">
</script>

<style>
* {
  font-family: 'Archivo', sans-serif;
}

.font-fraunces {
  font-family: 'Fraunces', serif !important;
}

html, body {
  height: 100dvh;
  overflow: hidden;
}
</style>
