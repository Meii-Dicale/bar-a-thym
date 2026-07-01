<template>
  <div v-if="totalPages > 1" class="d-flex align-center justify-center pt-6" style="gap: 8px;">
    <v-btn
      icon="mdi-chevron-left"
      variant="outlined"
      color="primary"
      size="small"
      rounded="lg"
      :disabled="page <= 1"
      @click="$emit('update:page', page - 1)"
    />

    <div class="d-flex" style="gap: 4px;">
      <template v-for="(item, i) in pages" :key="i">
        <span
          v-if="item === '…'"
          style="display: flex; align-items: center; padding: 0 4px; color: rgba(31,36,33,0.4);"
        >…</span>
        <v-btn
          v-else
          :variant="item === page ? 'flat' : 'text'"
          color="primary"
          size="small"
          rounded="lg"
          min-width="36"
          @click="$emit('update:page', item as number)"
        >
          {{ item }}
        </v-btn>
      </template>
    </div>

    <v-btn
      icon="mdi-chevron-right"
      variant="outlined"
      color="primary"
      size="small"
      rounded="lg"
      :disabled="page >= totalPages"
      @click="$emit('update:page', page + 1)"
    />
  </div>
</template>

<script setup lang="ts">
import { computed } from 'vue'

const props = defineProps<{
  page: number
  totalPages: number
}>()

defineEmits<{
  'update:page': [value: number]
}>()

const pages = computed((): (number | '…')[] => {
  const total = props.totalPages
  const current = props.page
  if (total <= 7) return Array.from({ length: total }, (_, i) => i + 1)
  if (current <= 4) return [1, 2, 3, 4, 5, '…', total]
  if (current >= total - 3) return [1, '…', total - 4, total - 3, total - 2, total - 1, total]
  return [1, '…', current - 1, current, current + 1, '…', total]
})
</script>

<style scoped>
</style>
