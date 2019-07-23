package main

import (
	"fmt"
	"math/rand"
	"time"
)

func genChoices(door int) []int {
	var a = rand.Intn(door)
	var c = make([]int, door, 100)
	c[a] = 1
	return c
}

func selectTarget(door int) int {
	var c = rand.Intn(door)
	return c
}

func revealFailure(choices []int, c1 int, door int) int {
	var s int
	for {
		s = rand.Intn(door)
		if s != c1 && choices[s] != 1 {
			break
		}
	}
	return s
}
func selectOther(c1 int, revealed int, door int) int {
	var s int
	for {
		s = rand.Intn(door)
		if c1 != s && revealed != s {
			break
		}
	}
	return s

}
func tryMonti(d int, trial int) (ratio1 float64, ratio2 float64) {

	for i := 0; i < trial; i++ {
		choices := genChoices(d)
		c1 := selectTarget(d)
		if choices[c1] == 1 {
			ratio1 += 1
		}
	}

	for i := 0; i < trial; i++ {
		choices := genChoices(d)
		c1 := selectTarget(d)
		n := revealFailure(choices, c1, d)
		c2 := selectOther(c1, n, d)

		if choices[c2] == 1 {
			ratio2 += 1
		}
	}

	ratio1 = ratio1 / float64(trial) * 100.0
	ratio2 = ratio2 / float64(trial) * 100.0

	return
}

func calcRatios(tri int, d int) {

	//var r1 []float64
	//var r2 []float64
	//var sum1 = 0
	//var sum2 = 0
	ra1, ra2 := tryMonti(d, tri)
	//r1 = append(r1, ra1)
	//r2 = append(r2, ra2)
	//
	//for i := 0; i<len(r1); i++ {
	//	sum1 += r1[i]
	//	sum2 += r2[i]
	//}

	//var s1 float64 = float64(float64(sum1) / float64(len(r1)))
	//var s2 float64 = float64(float64(sum2) / float64(len(r2)))

	fmt.Printf("変えない確率: %.2f%%\n" , ra1)
	fmt.Printf("変える確率: %.2f%%\n" , ra2)
}


func main() {

	var trial int
	var door int

	fmt.Print("trial: ")
	fmt.Scanf("%d", &trial)
	fmt.Print("door:")
	fmt.Scanf("%d", &door)

	b := time.Now()
	calcRatios(trial, door)
	f := time.Now()
	fmt.Println(f.Sub(b))

}
