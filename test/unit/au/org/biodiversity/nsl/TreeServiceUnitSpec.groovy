package au.org.biodiversity.nsl

import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.services.ServiceUnitTestMixin} for usage instructions
 */
@TestFor(TreeService)
@Mock([NameRank, NameGroup, Instance])
class TreeServiceUnitSpec extends Specification {

    def setup() {
        TestUte.setUpNameGroups()
        TestUte.setUpNameRanks()
    }

    def cleanup() {
    }

    void "test filter synonyms"() {

        when: 'I filter a synonyms map'
        Synonyms s = new Synonyms(synonyms)
        List<Synonym> result = s.filtered()
        println synonyms
        println result

        then: 'I get the result'
        result != null
        names.empty == result.empty
        if (!result.empty) {
            names.containsAll(result.collect { Synonym syn ->
                syn.simpleName
            })
        }

        where:
        names           | synonyms
        ['a', 'b', 'c'] | [[simple_name: 'a', type: 'taxonomic synonym', name_id: 1], [simple_name: 'b', type: 'nomenclatural synonym', name_id: 2], [simple_name: 'c', type: 'basionym', name_id: 3]]
        ['a']           | [[simple_name: 'a', type: "alternative name", name_id: 1]]
        ['a']           | [[simple_name: 'a', type: "basionym", name_id: 1]]
        ['b']           | [[simple_name: 'a', type: "common name", name_id: 1], [simple_name: 'b', type: 'taxonomic synonym', name_id: 1]]
        ['b']           | [[simple_name: 'a', type: "doubtful misapplied", name_id: 1], [simple_name: 'b', type: 'taxonomic synonym', name_id: 1]]
        []              | [[simple_name: 'a', type: "doubtful pro parte misapplied", name_id: 1]]
        []              | [[simple_name: 'a', type: "doubtful pro parte synonym", name_id: 1]]
        []              | [[simple_name: 'a', type: "doubtful pro parte taxonomic synonym", name_id: 1]]
        ['a']           | [[simple_name: 'a', type: "doubtful synonym", name_id: 1]]
        ['a']           | [[simple_name: 'a', type: "doubtful taxonomic synonym", name_id: 1]]
        ['a']           | [[simple_name: 'a', type: "isonym", name_id: 1]]
        []              | [[simple_name: 'a', type: "misapplied", name_id: 1]]
        ['a']           | [[simple_name: 'a', type: "nomenclatural synonym", name_id: 1]]
        ['a']           | [[simple_name: 'a', type: "orthographic variant", name_id: 1]]
        []              | [[simple_name: 'a', type: "pro parte misapplied", name_id: 1]]
        []              | [[simple_name: 'a', type: "pro parte synonym", name_id: 1]]
        []              | [[simple_name: 'a', type: "pro parte taxonomic synonym", name_id: 1]]
        ['a']           | [[simple_name: 'a', type: "replaced synonym", name_id: 1]]
        ['a']           | [[simple_name: 'a', type: "synonym", name_id: 1]]
        ['a']           | [[simple_name: 'a', type: "taxonomic synonym", name_id: 1]]
        ['a']           | [[simple_name: 'a', type: "trade name", name_id: 1]]
        []              | [[simple_name: 'a', type: "unsourced doubtful misapplied", name_id: 1]]
        []              | [[simple_name: 'a', type: "unsourced doubtful pro parte misapplied", name_id: 1]]
        []              | [[simple_name: 'a', type: "unsourced misapplied", name_id: 1]]
        []              | [[simple_name: 'a', type: "unsourced pro parte misapplied", name_id: 1]]
        []              | [[simple_name: 'a', type: "vernacular name", name_id: 1]]

    }

    void "test check polynomial below parent"() {
        when: 'I check a good placement'
        NameRank species = NameRank.findByName('Species')
        String[] parentNameElements = ['Plantae', 'Charophyta', 'Equisetopsida', 'Polypodiidae', 'Polypodiales', 'Blechnaceae', 'Doodia']
        service.checkPolynomialsBelowNameParent('Doodia aspera', false, species, parentNameElements)

        then: 'it works'
        notThrown(BadArgumentsException)

        when: 'I check a bad placement'
        parentNameElements = ['Plantae', 'Charophyta', 'Equisetopsida', 'Polypodiidae', 'Polypodiales', 'Blechnaceae', 'Blechnum']
        service.checkPolynomialsBelowNameParent('Doodia aspera', false, species, parentNameElements)

        then: 'it throws a bad argument exception'
        def e = thrown(BadArgumentsException)
        println e.message

        when: 'I check a hybrid name placement placed under the first parent'
        parentNameElements = ['Plantae', 'Charophyta', 'Equisetopsida', 'Polypodiidae', 'Polypodiales', 'Blechnaceae', 'Blechnum']
        service.checkPolynomialsBelowNameParent('Blechnum cartilagineum Sw. x Doodia media R.Br. ', false, species, parentNameElements)

        then: 'it works'
        notThrown(BadArgumentsException)

        when: 'I check a hybrid name placement placed under the second parent'
        parentNameElements = ['Plantae', 'Charophyta', 'Equisetopsida', 'Polypodiidae', 'Polypodiales', 'Blechnaceae', 'Doodia']
        service.checkPolynomialsBelowNameParent('Blechnum cartilagineum Sw. x Doodia media R.Br. ', false, species, parentNameElements)

        then: 'it throws a bad argument exception'
        thrown(BadArgumentsException)
    }

    void "test finding the rank of an element"() {
        when: 'I get the rank from the rank path for Blechnum'
        Map rankPath = [
                "Ordo"      : ["id": 223583, "name": "Polypodiales"],
                "Genus"     : ["id": 56340, "name": "Blechnum"],
                "Regnum"    : ["id": 54717, "name": "Plantae"],
                "Classis"   : ["id": 223519, "name": "Equisetopsida"],
                "Familia"   : ["id": 222592, "name": "Blechnaceae"],
                "Division"  : ["id": 224706, "name": "Charophyta"],
                "Subclassis": ["id": 224852, "name": "Polypodiidae"]
        ]
        NameRank rank = service.rankOfElement(rankPath, 'Blechnum')

        then: 'I get Genus'
        rank
        rank.name == 'Genus'

        when: 'I get the ranks of Doodia aspera from rankPath'
        rank = null
        rankPath = [
                "Ordo"      : ["id": 223583, "name": "Polypodiales"],
                "Genus"     : ["id": 70914, "name": "Doodia"],
                "Regnum"    : ["id": 54717, "name": "Plantae"],
                "Classis"   : ["id": 223519, "name": "Equisetopsida"],
                "Familia"   : ["id": 222592, "name": "Blechnaceae"],
                "Species"   : ["id": 70944, "name": "aspera"],
                "Division"  : ["id": 224706, "name": "Charophyta"],
                "Subclassis": ["id": 224852, "name": "Polypodiidae"]
        ]
        rank = service.rankOfElement(rankPath, 'aspera')

        then: 'I get Species'
        rank
        rank.name == 'Species'
    }

    void "test DisplayElement"() {
        when: "I create a DisplayElement from a list"
        DisplayElement displayElement = new DisplayElement(['display string', 'element link', 'name link', 'instance link', false, 8, 'synonyms html'])

        then: "it should work"
        displayElement
        !displayElement.excluded
        displayElement.instanceLink == 'instance link'
        displayElement.nameLink == 'name link'
        displayElement.displayHtml == 'display string'
        displayElement.elementLink == 'element link'
        displayElement.depth == 8
        displayElement.synonymsHtml == 'synonyms html'
    }

}
