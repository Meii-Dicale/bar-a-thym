import { onMounted, onUnmounted, type Ref } from 'vue'

const SEUIL = 60

export function useSwipe(
  el: Ref<HTMLElement | null>,
  onSwipeLeft: () => void,
  onSwipeRight: () => void
) {
  let touchStartX = 0

  function onTouchStart(e: TouchEvent) {
    touchStartX = e.touches[0].clientX
  }

  function onTouchEnd(e: TouchEvent) {
    const delta = touchStartX - e.changedTouches[0].clientX
    if (Math.abs(delta) < SEUIL) return
    if (delta > 0) onSwipeLeft()
    else onSwipeRight()
  }

  onMounted(() => {
    el.value?.addEventListener('touchstart', onTouchStart, { passive: true })
    el.value?.addEventListener('touchend', onTouchEnd, { passive: true })
  })

  onUnmounted(() => {
    el.value?.removeEventListener('touchstart', onTouchStart)
    el.value?.removeEventListener('touchend', onTouchEnd)
  })
}
